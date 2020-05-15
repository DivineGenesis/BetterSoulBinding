package xyz.divinegenesis.soulbound;


import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import xyz.divinegenesis.soulbound.config.SBConfig;
import xyz.divinegenesis.soulbound.data.IdentityData;
import xyz.divinegenesis.soulbound.data.IdentityKeys;
import xyz.divinegenesis.soulbound.eventlisteners.EventUtils;

import java.util.List;


public class CmdLoader {

    private static final Main plugin = Main.getInstance();

    private static SBConfig sbConfig = plugin.getSBConfig();

    private final EventUtils eventUtils = EventUtils.getEventUtils();

    private final Reference reference = Reference.getReference();

    private final Text craftKey = Text.of("BindType");

    public enum BindType {
        PICKUP(sbConfig.pickup.BindOnPickup),
        USE(sbConfig.use.BindOnUse),
        CRAFT(sbConfig.craft.BindOnCraft),
        REDEEM(sbConfig.nucleus.BindOnRedeem),
        INVENTORY_TRANSACTION(sbConfig.transaction.BindOnInventoryTransaction);

        public final List<String> config;

        BindType (List<String> config) {

            this.config = config;
        }
    }

    private CommandSpec addConfig = CommandSpec.builder()
            .description(Text.of("Adds items to soulbind into config"))
            .executor(this::add_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.enumValue(Text.of(this.craftKey), BindType.class)))
            .permission(reference.ADD_LIST)
            .build();

    private CommandSpec removeConfig = CommandSpec.builder()
            .description(Text.of("Removes items to soulbind from the config"))
            .executor(this::remove_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.enumValue(Text.of(this.craftKey), BindType.class)))
            .permission(reference.REMOVE_LIST)
            .build();

    private CommandSpec Config = CommandSpec.builder()
            .description(Text.of("Child argument for Config changes"))
            .child(addConfig, "add", "a")
            .child(removeConfig, "remove", "r")
            .build();

    private CommandSpec addSB = CommandSpec.builder()
            .description(Text.of("Binds the item to the player holding it"))
            .executor(this::add_sb)
            .permission(reference.ADD_SB)
            .build();

    private CommandSpec removeSB = CommandSpec.builder()
            .description(Text.of("Removes the binding from the item"))
            .executor(this::remove_sb)
            .permission(reference.REMOVE_SB)
            .build();

    private CommandSpec soulbound = CommandSpec.builder().child(addSB, "add", "a").child(removeSB, "remove", "r").build();

    //Base Command for above commands, as commands are added, create additional children

    CommandSpec sb = CommandSpec.builder()
            .description(Text.of("Shows this list"))
            .executor(this::help)
            .permission(reference.HELP)
            .child(Config, "config", "c")
            .child(soulbound, "soulbound", "bind", "b")
            .build();

    private CommandResult help (CommandSource src, CommandContext args) throws CommandException {

        List<Text> commandHelp = Lists.newArrayList();
        commandHelp.add(helpTextStructure("config", plugin.getMessagesConfig().help_menu.HELP_MENU_ADD_CONFIG));
        commandHelp.add(helpTextStructure("soulbound", plugin.getMessagesConfig().help_menu.HELP_MENU_REMOVE_SOULBOUND));

        PaginationList.builder()
                .title(Text.of(TextColors.GOLD, "BetterSoulbinding Help Menu"))
                .padding(Text.of(TextColors.YELLOW, TextStyles.STRIKETHROUGH, '='))
                .contents(commandHelp)
                .sendTo(src);
        return CommandResult.success();
    }

    private Text helpTextStructure (String command, String reason) {

        return Text.of(TextColors.GREEN, Text.builder(command)
                .onClick(TextActions.suggestCommand("/sb " + command + " add|remove")), TextColors.GRAY, TextStyles.ITALIC, " - ", reason);
    }

    private CommandResult add_list (CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            String arg = args.getOne(this.craftKey).get().toString();

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                String id = eventUtils.getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
                listFunction(arg, 'a', src, id, "added");
            }

        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, plugin.getMessagesConfig().COMMAND_MUST_BE_RUN_BY_PLAYER));
        }
        return CommandResult.success();
    }

    private CommandResult remove_list (CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            String arg = args.getOne(this.craftKey).get().toString();
            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !arg.equals("")) {
                String id = eventUtils.getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
                listFunction(arg, 'r', src, id, "removed");
            }
        } else {
            src.sendMessage(Text.of(TextColors.DARK_RED, plugin.getMessagesConfig().COMMAND_MUST_BE_RUN_BY_PLAYER));
        }
        return CommandResult.success();
    }

    private void listFunction (String arg, char type, CommandSource src, String id, String action) {

        for (BindType b : BindType.values()) {
            if (arg.equalsIgnoreCase(b.name())) {
                if (modifyList(id, b.ordinal(), type)) {
                    successMessage(src, action, id, b.name(), type);
                    plugin.saveConfig();
                } else {
                    errorMessage(src, id, b.name(), type);
                }
            }
        }
    }

    private void successMessage (CommandSource src, String action, String id, String list, char type) {

        String actionType = "from";
        if (type == 'a') {
            actionType = "to";
        }
        src.sendMessage(Text.of(TextColors.GREEN, "Succesfully ", action, ' ', TextColors.WHITE, id, TextColors.GREEN, ' ', actionType, " the ", list, ' ', "list!"));
    }

    private void errorMessage (CommandSource src, String id, String configType, char type) {

        String exist = "doesn't";
        if (type == 'a') {
            exist = "already";
        }

        src.sendMessage(Text.of(Text.of(TextColors.RED, id, ' ', exist, " exist(s) in the ", configType, " list!")));
    }

    private CommandResult add_sb (CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;


            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
                if (stack.get(IdentityKeys.IDENTITY).isPresent()) {
                    player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_ALREADY_BOUND));
                    return CommandResult.success();
                }
                eventUtils.applyData(player, stack);
                stack.offer(IdentityKeys.IDENTITY, reference.Blank_UUID);
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
                player.sendMessage(Text.of(TextColors.GREEN, plugin.getMessagesConfig().items.ITEM_SUCCESSFULLY_BOUND));
            }
        }
        return CommandResult.success();
    }

    private CommandResult remove_sb (CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();

                if (stack.get(IdentityKeys.IDENTITY).isPresent()) {
                    stack.remove(IdentityKeys.IDENTITY);
                    stack.remove(IdentityData.class);
                    if (stack.get(Keys.ITEM_LORE).isPresent()) {
                        stack.remove(Keys.ITEM_LORE);
                    }
                    player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    player.sendMessage(Text.of(TextColors.GREEN, plugin.getMessagesConfig().items.ITEM_SUCCESSFULLY_REMOVED_BIND));
                } else {
                    player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_NOT_BOUND));
                }
            } else {
                player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_NOT_PRESENT));
            }
        }
        return CommandResult.success();
    }


    private boolean modifyList (String id, int index, char addType) {

        for (BindType bindType : BindType.values()) {
            if (index == bindType.ordinal()) {
                if (addType == 'a') {
                    if (bindType.config.contains(id)) {
                        return false;
                    }
                    bindType.config.add(id);
                    return true;
                } else {
                    if (bindType.config.contains(id)) {
                        bindType.config.remove(id);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}