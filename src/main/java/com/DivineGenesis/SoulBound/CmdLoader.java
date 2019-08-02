package com.DivineGenesis.SoulBound;


import com.DivineGenesis.SoulBound.config.SBConfig;
import com.DivineGenesis.SoulBound.data.IdentityData;
import com.DivineGenesis.SoulBound.eventlisteners.EventUtils;
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

import java.util.List;

import static com.DivineGenesis.SoulBound.Reference.*;
import static com.DivineGenesis.SoulBound.data.IdentityKeys.IDENTITY;


public class CmdLoader {

    public enum BindType {
        PICKUP(0), USE(1), CRAFT(2);
        public final int type;

        BindType (int type) {

            this.type = type;
        }
    }

    private final Text craftKey = Text.of("BindType");
    private final Text playerKey = Text.of("Player");
    private static final Main plugin = Main.getInstance();


    private CommandSpec addConfig = CommandSpec.builder()
            .description(Text.of("Adds items to soulbind into config"))
            .executor(this::add_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.enumValue(Text.of(this.craftKey), BindType.class)))
            .permission(ADD_LIST)
            .build();

    private CommandSpec removeConfig = CommandSpec.builder()
            .description(Text.of("Removes items to soulbind from the config"))
            .executor(this::remove_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.enumValue(Text.of(this.craftKey), BindType.class)))
            .permission(REMOVE_LIST)
            .build();

    private CommandSpec addSB = CommandSpec.builder()
            .description(Text.of("Binds the item to the player holding it"))
            .executor(this::add_sb)
            .permission(ADD_SB)
            .build();

    private CommandSpec removeSB = CommandSpec.builder()
            .description(Text.of("Removes the binding from the item"))
            .executor(this::remove_sb)
            .permission(REMOVE_SB)
            .build();

    //Base Command for above commands, as commands are added, create additional children

    CommandSpec sb = CommandSpec.builder()
            .description(Text.of("Shows this list"))
            .executor(this::help)
            .permission(HELP)
            .child(addConfig, "addConfig", "ac")
            .child(removeConfig, "removeConfig", "rc")
            .child(addSB, "addSoulBound", "addSB", "as")
            .child(removeSB, "removeSoulBound", "removeSB", "rs")
            .build();

    private CommandResult help (CommandSource src, CommandContext args) throws CommandException {

        List<Text> commandHelp = Lists.newArrayList();
        commandHelp.add(helpTextStructure("addConfig", plugin.getMessagesConfig().help_menu.HELP_MENU_ADD_CONFIG));
        commandHelp.add(helpTextStructure("removeConfig", plugin.getMessagesConfig().help_menu.HELP_MENU_REMOVE_CONFIG));
        commandHelp.add(helpTextStructure("addSB", plugin.getMessagesConfig().help_menu.HELP_MENU_ADD_SOULBOUND));
        commandHelp.add(helpTextStructure("removeSB", plugin.getMessagesConfig().help_menu.HELP_MENU_REMOVE_SOULBOUND));

        PaginationList.builder()
                .title(Text.of(TextColors.GOLD, "BetterSoulbinding Help Menu"))
                .padding(Text.of(TextColors.YELLOW, TextStyles.STRIKETHROUGH, '='))
                .contents(commandHelp)
                .sendTo(src);
        return CommandResult.success();
    }

    private Text helpTextStructure (String command, String reason) {

        return Text.of(TextColors.GREEN, Text.builder(command)
                .onClick(TextActions.suggestCommand("/sb " + command)), TextColors.GRAY, TextStyles.ITALIC, " - ", reason);
    }

    private CommandResult add_list (CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            String arg = args.getOne(this.craftKey).get().toString();

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                String id = getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
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
                String id = getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
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
                if (modifyList(id, b.type, type)) {
                    successMessage(src, action, id, b.name(), type);
                    plugin.saveConfig();
                } else {
                    errorMessage(src, id, b.name(), type);
                }
            }
        }
    }

    private static void successMessage (CommandSource src, String action, String id, String list, char type) {

        String actionType = "from";
        if (type == 'a') {
            actionType = " to";
        }
        src.sendMessage(Text.of(TextColors.GREEN, "Succesfully ", action, ' ', TextColors.WHITE, id, TextColors.GREEN, ' ', actionType, " the ", list, ' ', "list!"));
    }

    private static void errorMessage (CommandSource src, String id, String configType, char type) {

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
                if (stack.get(IDENTITY).isPresent()) {
                    player.sendMessage(Text.of(TextColors.RED, plugin.getMessagesConfig().items.ITEM_ALREADY_BOUND));
                    return CommandResult.success();
                }
                EventUtils.applyData(player, stack);
                stack.offer(IDENTITY, Blank_UUID);
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

                if (stack.get(IDENTITY).isPresent()) {
                    stack.remove(IDENTITY);
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


    private static boolean modifyList (String id, int index, char addType) {

        SBConfig config = plugin.getSBConfig();

        switch (index) {
            case 0: //pick up
                if (addType == 'a') { //If adding item
                    if (config.BindOnPickup.contains(id)) {
                        return false;
                    }
                    config.BindOnPickup.add(id);
                    return true;
                } else { //else we're removing the item
                    if (config.BindOnPickup.contains(id)) {
                        config.BindOnPickup.remove(id);
                        return true;
                    }
                    return false;
                }

            case 1: //use
                if (addType == 'a') {
                    if (config.BindOnUse.contains(id)) {
                        return false;
                    }
                    config.BindOnUse.add(id);
                    return true;
                } else {
                    if (config.BindOnUse.contains(id)) {
                        config.BindOnUse.remove(id);
                        return true;
                    }
                    return false;
                }

            case 2: //Craft
                if (addType == 'a') {
                    if (config.BindOnCraft.contains(id)) {
                        return false;
                    }
                    config.BindOnCraft.add(id);
                    return true;
                } else {
                    if (config.BindOnCraft.contains(id)) {
                        config.BindOnCraft.remove(id);
                        return true;
                    }
                    return false;
                }
        }
        return false;
    }


}