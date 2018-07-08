package com.DivineGenesis.SoulBound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.DivineGenesis.SoulBound.config.SBConfig;
import com.google.common.collect.Lists;
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
import static com.DivineGenesis.SoulBound.utils.SBUtil.*;

public class CmdLoader {
	
	//Permissions
		//users
		public static final String PICKUP = "soulbound.user.pickup";
		public static final String USE = "soulbound.user.use";
		public static final String KEEP_ON_DEATH = "soulbound.user.keep";
		public static final String CRAFT = "soulbound.user.craft";

		//admins
		static final String HELP = "soulbound.admin.help";
		static final String ADD_LIST = "soulbound.admin.addlist";
		static final String REMOVE_LIST = "soulbound.admin.removelist";
		static final String ADD_SB = "soulbound.admin.addsb";
		static final String REMOVE_SB = "soulbound.admin.removesb";
		
    private String craftType = "pickup|use|craft";

    private CommandSpec addConfig = CommandSpec.builder()
            .description(Text.of("Adds items to soulbind into config")).executor(this::add_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of(craftType))))
            .permission(ADD_LIST).build();

    private CommandSpec removeConfig = CommandSpec.builder()
            .description(Text.of("Removes items to soulbind from the config")).executor(this::remove_list)
            .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of(craftType))))
            .permission(REMOVE_LIST).build();

    private CommandSpec addSB = CommandSpec.builder()
            .description(Text.of("Binds the item to the player holding it")).executor(this::add_sb)
            .permission(ADD_SB).build();

    private CommandSpec removeSB = CommandSpec.builder()
            .description(Text.of("Removes the binding from the item")).executor(this::remove_sb)
            .permission(REMOVE_SB).build();


    CommandSpec sb = CommandSpec.builder()
            .description(Text.of("Shows this list")).executor(this::help).permission(HELP)
            .child(addConfig,"addConfig")
            .child(removeConfig,"removeConfig")
            .child(addSB,"addSB")
            .child(removeSB,"removeSB")
            .build();

    private CommandResult help (CommandSource src,CommandContext args) {
        List<Text> commandHelp = Lists.newArrayList();
        commandHelp.add(helpTextStructure("addConfig","Adds items to allow soulbinding into config!"));
        commandHelp.add(helpTextStructure("removeConfig","Removes items from the config!"));
        commandHelp.add(helpTextStructure("addSB","Bind the item to the player holding it!"));
        commandHelp.add(helpTextStructure("removeSB","Removes the binding from the item!"));

        PaginationList.builder()
                .title(Text.of(TextColors.GOLD,"BetterSoulbinding Help Menu"))
                .padding(Text.of(TextColors.YELLOW,TextStyles.STRIKETHROUGH,'='))
                .contents(commandHelp)
                .sendTo(src);
        return CommandResult.success();
    }

    private Text helpTextStructure (String command,String reason) {
        return Text.of(TextColors.GREEN,Text.builder(command).onClick(TextActions.suggestCommand("/sb " + command)),
                TextColors.GRAY,TextStyles.ITALIC," - ",reason);
    }

    private CommandResult add_list (CommandSource src,CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;
            String arg = args.getOne(craftType).get().toString();

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !arg.equals("")) {
                String id = getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
                listFunction(arg,'a',src,id,"added");
            } else
                src.sendMessage(Text.of(TextColors.DARK_RED,"INVALID ARGUMENTS: ",TextColors.GOLD,craftType));
        }
        return CommandResult.success();
    }

    private CommandResult remove_list (CommandSource src,CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            String arg = args.getOne(craftType).get().toString();
            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !arg.equals("")) {
                String id = getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
                listFunction(arg,'r',src,id,"removed");
            } else
                src.sendMessage(Text.of(TextColors.DARK_RED,"INVALID ARGUMENTS: ",TextColors.GOLD,craftType));
        }
        return CommandResult.success();
    }

    private void listFunction (String arg,char type,CommandSource src,String id,String action) {
        List<String> bindTypes = new ArrayList<>(Arrays.asList("pickup","use","craft"));
        for (int i = 0; i < bindTypes.size(); i++) {
            if (arg.equalsIgnoreCase(bindTypes.get(i))) {
                if (type == 'a')
                    if (addToList(id,i))
                        mailMan(src,action,id,bindTypes.get(i));
                    else
                        errorMessage(src,id,"already",bindTypes.get(i));
            } else if (type == 'r') {
                if (removeFromList(id,i))
                    mailMan(src,action,id,bindTypes.get(i));
                else
                    errorMessage(src,id,"doesn't",bindTypes.get(i));
            }
        }
    }

    private static void mailMan (CommandSource src,String action,String id,String type) {
        src.sendMessage(Text.of(TextColors.GREEN,"Succesfully ",action,' ',TextColors.WHITE,id,
                TextColors.GREEN," to/from the ",type," list!"));
    }

    private static void errorMessage (CommandSource src,String id,String exist,String configType) {
        src.sendMessage(Text.of(Text.of(TextColors.RED,id,exist," exist(s) in the ",configType," list!")));
    }

    private CommandResult add_sb (CommandSource src,CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
                List<Text> loreList = new ArrayList<>();
                loreList.add(Text.of("Bound to: none"));
                stack.offer(Keys.ITEM_LORE,loreList);
                player.setItemInHand(HandTypes.MAIN_HAND,stack);
            }
        }
        return CommandResult.success();
    }

    private CommandResult remove_sb (CommandSource src,CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player) src;

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
                List<Text> loreList = stack.get(Keys.ITEM_LORE).get();
                if (!stack.get(Keys.ITEM_LORE).isPresent()) {
                    player.sendMessage(Text.of(TextColors.DARK_RED,"This item does not contain any lore!"));
                } else
                    for (int i = 0; i < getLore(stack).size(); i++) {
                        if (getLore(stack).get(i).toPlain().startsWith("Bound to:")) {
                            loreList.remove(i);
                            stack.offer(Keys.ITEM_LORE,loreList);
                            player.setItemInHand(HandTypes.MAIN_HAND,stack);
                            if (getLore(stack).get(i).toPlain().startsWith("UUID:")) {
                                loreList.remove(i);
                                if (loreList.isEmpty())
                                    stack.remove(Keys.ITEM_LORE);
                                else
                                    stack.offer(Keys.ITEM_LORE,loreList);
                                player.setItemInHand(HandTypes.MAIN_HAND,stack);
                                player.sendMessage(Text.of(TextColors.GREEN,"Item has been successfully unbound!"));
                            }
                            break;
                        } else if (i == loreList.size() - 1) {
                            player.sendMessage(Text.of(TextColors.DARK_RED,"This item is not bound to anyone!"));
                            break;
                        }
                    }
            }
        }
        return CommandResult.success();
    }

    private static boolean addToList (String id,int index) {
        switch (index) {
            case 0: //pick up
                if (SBConfig.sb_pickup.contains(id))
                    return false;
                SBConfig.sb_pickup.add(id);
                SBConfig.saveToFile();
                return true;

            case 1: //use
                if (SBConfig.sb_use.contains(id))
                    return false;
                SBConfig.sb_use.add(id);
                SBConfig.saveToFile();
                return true;

            case 2: //Craft
                if (SBConfig.sb_craft.contains(id))
                    return false;
                SBConfig.sb_craft.add(id);
                SBConfig.saveToFile();
                return true;
        }
        return false;
    }

    private static boolean removeFromList (String id,int index) {
        switch (index) {
            case 0:
                if (SBConfig.sb_pickup.contains(id)) {
                	SBConfig.sb_pickup.remove(id);
                    SBConfig.saveToFile();
                    return true;
                }
                return false;
            case 1:
                if (SBConfig.sb_use.contains(id)) {
                	SBConfig.sb_use.remove(id);
                    SBConfig.saveToFile();
                    return true;
                }
                return false;

            case 2:
                if (SBConfig.sb_craft.contains(id)) {
                	SBConfig.sb_craft.remove(id);
                    SBConfig.saveToFile();
                    return true;
                }
        }
        return false;
    }
}