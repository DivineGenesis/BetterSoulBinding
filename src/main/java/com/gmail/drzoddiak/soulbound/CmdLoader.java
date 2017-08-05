package com.gmail.drzoddiak.soulbound;

import java.util.ArrayList;
import java.util.List;

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

import com.google.common.collect.Lists;

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
        		String id = args.getOne("id").toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && id != "")
        		{
        			if(id.equalsIgnoreCase("pickup"))
        			{
        				if( Reference.addToList(id, 0) )
        					src.sendMessage(Text.of("Successfully added to pickupList!"));
        				else
        					src.sendMessage(Text.of("Already exists in pickupList!"));
        			}

        			if(id.equalsIgnoreCase("use"))
        			{
        				if(Reference.addToList(id, 1))
        					src.sendMessage(Text.of("Successfully added to useList!"));
        				else
        					src.sendMessage(Text.of("Already exists in useList!"));
        			}
        			
        			if(id.equalsIgnoreCase("equip"))
        			{
        				if(Reference.addToList(id, 2))
        					src.sendMessage(Text.of("Successfully added to equipList!"));
        				else
        					src.sendMessage(Text.of("Already exists in equipList!"));
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
        		String id = args.getOne("id").toString();
        		
        		if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() && id != "")
        		{
        			if(id.equalsIgnoreCase("pickup"))
        			{
        				if(Reference.removeFromList(id, 0))
        					src.sendMessage(Text.of("Successfully removed from pickupList!"));
        				else
        					src.sendMessage(Text.of("Doesn't exist in pickupList!"));
        			}
        			
        			if(id.equalsIgnoreCase("use"))
        			{
        				if(Reference.removeFromList(id, 1))
        					src.sendMessage(Text.of("Successfully removed from useList!"));
        				else
        					src.sendMessage(Text.of("Doesn't exist in useList!"));
        			}
        			
        			if(id.equalsIgnoreCase("equip"))
        			{
        				if(Reference.removeFromList(id, 2))
        					src.sendMessage(Text.of("Successfully removed to equipList!"));
        				else
        					src.sendMessage(Text.of("Doesn't exist in equipList!"));
        			}
        			
        		}
        			
        	}
        	return CommandResult.success();
        }
        
        public CommandResult add_sb(CommandSource src, CommandContext args) throws CommandException
        {
        	if(src instanceof Player)
        	{
        		Player player = (Player)src;
        		
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