package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import javafx.scene.text.TextAlignment;
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

import static com.gmail.drzoddiak.soulbound.Reference.getLore;

public class CmdLoader
{
    private CommandSpec addConfig = CommandSpec.builder()//Possibly allow for more than 1 ohoice or all in the future!
            .description(Text.of("Adds items to soulbind into config")).executor( this::add_list ).arguments(GenericArguments.onlyOne(GenericArguments
            .string(Text.of("id")))).permission(Reference.ADD_LIST).build();

    private CommandSpec removeConfig = CommandSpec.builder()
    		.description(Text.of("Removes items to soulbind from the config")).executor( this::remove_list ).arguments(GenericArguments.onlyOne(GenericArguments
    		.string(Text.of("id")))).permission(Reference.REMOVE_LIST).build();
        
    private CommandSpec addSB = CommandSpec.builder()
            .description(Text.of("Binds the item to the player holding it")).executor(this::add_sb).permission(Reference.ADD_SB).build();

    private CommandSpec removeSB = CommandSpec.builder()
                .description(Text.of("Removes the binding from the item")).executor(this::remove_sb).permission(Reference.REMOVE_SB).build();

            //Base Command for above commands, as commands are added, create additional children

        public CommandSpec sb = CommandSpec.builder()
            .description(Text.of("Shows this list")).executor(this::help).permission(Reference.HELP)
                .child(addConfig, "add")
                .child(removeConfig, "remove")
                .child(addSB, "addSB")
                .child(removeSB, "removeSB")
                .build();
        
        public CommandResult help(CommandSource src, CommandContext args) throws CommandException
        {
            List<Text> commandHelp = Lists.newArrayList();
        	commandHelp.add(textStructure("addList","Adds items to allow soulbinding into config!"));
			commandHelp.add(textStructure("removeList","Removes items from the config!"));
			commandHelp.add(textStructure("addSB","Bind the item to the player holding it!"));
			commandHelp.add(textStructure("removeSB","Removes the binding from the item!"));

			PaginationList.builder()
					.title(Text.of(TextColors.GOLD, "BetterSoulbinding Help Menu"))
					.padding(Text.of(TextColors.YELLOW, TextStyles.STRIKETHROUGH,'='))
					.contents(commandHelp)
					.sendTo(src);
			//triggered when they type only the base cmd /sb
        	return CommandResult.success();
        }
        
        public CommandResult add_list(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
				String arg = args.getOne("id").get().toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !arg.equals(""))
        		{
        			String id = Reference.getID(player.getItemInHand(HandTypes.MAIN_HAND).get()); 
        			
        			if(arg.equalsIgnoreCase("pickup"))
        			{
        				if( Reference.addToList(id, 0) )
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully added ", TextColors.WHITE, id, TextColors.GREEN," to the 'pickup' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Already exists in the 'pickup' list!"));
        			}
        			else if(arg.equalsIgnoreCase("use"))
        			{
        				if(Reference.addToList(id, 1))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully added ", TextColors.WHITE, id, TextColors.GREEN," to 'use' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Already exists in the 'use' list!"));
        			}
        			else if(arg.equalsIgnoreCase("equip"))
        			{
        				if(Reference.addToList(id, 2))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully added ", TextColors.WHITE, id, TextColors.GREEN," to the 'equip' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Already exists in the 'equip' list!"));
        			}
        		}
        			
        	}
        	return CommandResult.success();
        }
        private Text textStructure(String command, String reason)
		{
			return Text.of(TextColors.GREEN, Text.builder(command).onClick(TextActions.suggestCommand("/sb "+command)),
                    TextColors.GRAY,TextStyles.ITALIC," - ", reason);
		}

		public CommandResult remove_list(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
        		String arg = args.getOne("id").get().toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && !arg.equals(""))
        		{
        			String id = Reference.getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
        			
        			if(arg.equalsIgnoreCase("pickup"))
        				if(Reference.removeFromList(id, 0))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from the 'pickup' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Doesn't exist in the 'pickup' list!"));
        			
        			if(arg.equalsIgnoreCase("use"))
        				if(Reference.removeFromList(id, 1))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from 'use' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Doesn't exist in the 'use' list!"));

        			if(arg.equalsIgnoreCase("equip"))
        				if(Reference.removeFromList(id, 2))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from the 'equip' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," doesn't exist in the 'equip' list!"));
        		}
        	}
        	return CommandResult.success();
        }

        public CommandResult add_sb(CommandSource src, CommandContext args) throws CommandException
        {//Allow user to select user to add to item in future?
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
        		{
        			ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
        			List<Text> loreList = new ArrayList<>();
        			loreList.add(Text.of("Bound to: "+player.getName()));
        			loreList.add(Text.of("UUID: "+player.getUniqueId()));
        			stack.offer(Keys.ITEM_LORE, loreList);
        			player.setItemInHand(HandTypes.MAIN_HAND, stack);
        		}
        	}
        	return CommandResult.success();
        }
        
        public CommandResult remove_sb(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player)src;
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
        		{
                    ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
					List<Text> loreList = stack.get(Keys.ITEM_LORE).get();
                    if(!stack.get(Keys.ITEM_LORE).isPresent())
                    {
                    	player.sendMessage(Text.of(TextColors.DARK_RED, "This item does not contain any lore!"));
                    }
                    else
                    	for(int i=0; i<getLore(stack).size();i++)
                    	{
                    		if(getLore(stack).get(i).toPlain().startsWith("Bound to:"))
                    		{
                    			loreList.remove(i);
                    			stack.offer(Keys.ITEM_LORE, loreList);
                    			player.setItemInHand(HandTypes.MAIN_HAND, stack);

                    			if(getLore(stack).get(i).toPlain().startsWith("UUID:"))
                    			{
                    				loreList.remove(i);
                    				if(loreList.isEmpty())
                    					stack.remove(Keys.ITEM_LORE);
                    				else
                    					stack.offer(Keys.ITEM_LORE, loreList);
                    				player.setItemInHand(HandTypes.MAIN_HAND, stack);
                    				player.sendMessage(Text.of(TextColors.GREEN, "Item has been successfully unbound!"));
                    			}
                    			break;
                    		}
                    		else if(i == loreList.size()-1)
                    		{
                    			player.sendMessage(Text.of(TextColors.DARK_RED, "This item is not bound to anyone!"));
                    			break;
                    		}
                    	}
        		}
        	}
        	return CommandResult.success();
        }
}