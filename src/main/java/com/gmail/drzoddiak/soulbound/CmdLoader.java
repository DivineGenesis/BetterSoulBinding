package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CmdLoader
{
    private CommandSpec addList = CommandSpec.builder()
            .description(Text.of("")).executor( this::add_list ).arguments(GenericArguments.onlyOne(GenericArguments
            .string(Text.of("id")))).permission(Reference.ADD_LIST).build();

    private CommandSpec removeList = CommandSpec.builder()
    		.description(Text.of("")).executor( this::remove_list ).arguments(GenericArguments.onlyOne(GenericArguments
    		.string(Text.of("id")))).permission(Reference.REMOVE_LIST).build();
        
    private CommandSpec addSB = CommandSpec.builder()
            .description(Text.of("")).executor(this::add_sb).permission(Reference.ADD_SB).build();

    private CommandSpec removeSB = CommandSpec.builder()
                .description(Text.of("")).executor(this::remove_sb).permission(Reference.REMOVE_SB).build();

            //Base Command for above commands, as commands are added, create additional children

        public CommandSpec sb = CommandSpec.builder()
            .description(Text.of("Base command")).executor(this::help).permission(Reference.HELP)
                .child(addList, "add")
                .child(removeList, "remove")
                .child(addSB, "addSB")
                .child(removeSB, "removeSB")
                .build();
        
        public CommandResult help(CommandSource src, CommandContext args) throws CommandException
        {		
        	//triggered when they type only the base cmd /sb
        	return CommandResult.success();
        }
        
        public CommandResult add_list(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
        		String arg = args.getOne("id").get().toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && arg != "")
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
        
        public CommandResult remove_list(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
        		String arg = args.getOne("id").get().toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && arg != "")
        		{
        			String id = Reference.getID(player.getItemInHand(HandTypes.MAIN_HAND).get());
        			
        			if(arg.equalsIgnoreCase("pickup"))
        			{
        				if(Reference.removeFromList(id, 0))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from the 'pickup' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Doesn't exist in the 'pickup' list!"));
        			}
        			
        			if(arg.equalsIgnoreCase("use"))
        			{
        				if(Reference.removeFromList(id, 1))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from 'use' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," Doesn't exist in the 'use' list!"));
        			}
        			
        			if(arg.equalsIgnoreCase("equip"))
        			{
        				if(Reference.removeFromList(id, 2))
        					src.sendMessage(Text.of(TextColors.GREEN,"Successfully removed ", TextColors.WHITE, id, TextColors.GREEN," from the 'equip' list!"));
        				else
        					src.sendMessage(Text.of(id,TextColors.RED," doesn't exist in the 'equip' list!"));
        			}
        			
        		}
        			
        	}
        	return CommandResult.success();
        }
        
        public CommandResult add_sb(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player) src;
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
        		{
        			ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
        			List<Text> loreList = new ArrayList<Text>();
        			loreList.add(Text.of("Bounded to: "+player.getName()));
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
        		}
        	}
        	return CommandResult.success();
        }
}