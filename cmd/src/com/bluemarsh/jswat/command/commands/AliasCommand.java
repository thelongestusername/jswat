/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is the JSwat Command Module. The Initial Developer of the
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2005. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.jswat.command.commands;

import com.bluemarsh.jswat.command.AbstractCommand;
import com.bluemarsh.jswat.command.CommandArguments;
import com.bluemarsh.jswat.command.CommandContext;
import com.bluemarsh.jswat.command.CommandException;
import com.bluemarsh.jswat.command.CommandParser;
import com.bluemarsh.jswat.command.MissingArgumentsException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.openide.util.NbBundle;

/**
 * Displays the command aliases, and permits adding and removing aliases.
 *
 * @author Nathan Fiedler
 */
public class AliasCommand extends AbstractCommand {

    public String getName() {
        return "alias";
    }

    public void perform(CommandContext context, CommandArguments arguments)
            throws CommandException, MissingArgumentsException {

        CommandParser parser = context.getParser();
        PrintWriter writer = context.getWriter();
        if (arguments.hasMoreTokens()) {
            String name = arguments.nextToken();
            if (arguments.hasMoreTokens()) {
                // Grab rest of string as alias value.
                // Be sure to preserve the quotes and escapes.
                arguments.returnAsIs(true);
                String alias = arguments.rest().trim();
                if (alias.charAt(0) == '"'
                    && alias.charAt(alias.length() - 1) == '"') {
                    // Must remove the enclosing quotes because the
                    // command parser does not handle that.
                    alias = alias.substring(1, alias.length() - 1);
                }
                parser.setAlias(name, alias);
            } else {
                // One argument, show the alias definition.
                String alias = parser.getAlias(name);
                if (alias == null) {
                    throw new CommandException(
                        NbBundle.getMessage(getClass(), "ERR_Alias_Undefined", name));
                } else {
                    writer.println(NbBundle.getMessage(getClass(),
                            "CTL_Alias_Definition", name, alias));
                }
            }

        } else {
            // No arguments, show the defined aliases.
            Iterator<String> aliases = parser.getAliases();
            if (aliases.hasNext()) {
                List<String> list = new ArrayList<String>();
                while (aliases.hasNext()) {
                    list.add(aliases.next());
                }
                Collections.sort(list);
                aliases = list.iterator();
                StringBuilder sb = new StringBuilder();
                while (aliases.hasNext()) {
                    String name = aliases.next();
                    String value = parser.getAlias(name);
                    sb.append(name);
                    if (name.length() >= 8) {
                        sb.append('\t');
                    } else {
                        sb.append("\t\t");
                    }
                    sb.append(value);
                    sb.append('\n');
                }
                writer.write(sb.toString());
            }
        }
    }
}
