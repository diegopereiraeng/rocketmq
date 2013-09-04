package com.alibaba.rocketmq.tools.command.namesrv;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.namesrv.NamesrvUtil;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.SubCommand;


/**
 * 删除 project group 配置信息
 * 
 * @author: manhong.yqd<jodie.yqd@gmail.com>
 * @since: 13-8-29
 */
public class DeleteProjectGroupCommand implements SubCommand {
    @Override
    public String commandName() {
        return "deleteProjectGroup";
    }


    @Override
    public String commandDesc() {
        return "delete project group by server ip.";
    }


    @Override
    public Options buildCommandlineOptions(Options options) {
        Option opt = new Option("i", "ip", true, "set the server ip");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("p", "project", true, "set the project group");
        opt.setRequired(false);
        options.addOption(opt);
        return options;
    }


    @Override
    public void execute(CommandLine commandLine, Options options) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        try {
            String namespace = NamesrvUtil.NAMESPACE_PROJECT_CONFIG;

            if (commandLine.hasOption("i")) {
                String ip = commandLine.getOptionValue('i').trim();
                defaultMQAdminExt.start();
                defaultMQAdminExt.deleteKvConfig(namespace, ip);
                System.out.printf("delete project group from namespace by server ip success.\n");
            }
            else if (commandLine.hasOption("p")) {
                String project = commandLine.getOptionValue('p').trim();
                defaultMQAdminExt.start();
                defaultMQAdminExt.deleteIpsByProjectGroup(project);
                System.out.printf("delete all server ip from namespace by project group success.\n");
            }
            else {
	            MixAll.printCommandLineHelp("mqadmin " + this.commandName(), options);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
    public static void main(String[] args) {
        System.setProperty(MixAll.NAMESRV_ADDR_PROPERTY, "127.0.0.1:9876");
        DeleteProjectGroupCommand cmd = new DeleteProjectGroupCommand();
        Options options = MixAll.buildCommandlineOptions(new Options());
        String[] subargs = new String[] { "-i 10.14.24.165 ","-p devgrouptest" };
        final CommandLine commandLine =
                MixAll.parseCmdLine("mqadmin " + cmd.commandName(), subargs,
                    cmd.buildCommandlineOptions(options), new PosixParser());
        cmd.execute(commandLine, options);
    }
}
