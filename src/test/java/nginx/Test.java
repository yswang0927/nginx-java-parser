package nginx;

import com.github.odiszapc.nginxparser.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
        try (InputStream is = Test.class.getClassLoader().getResourceAsStream("nginx2.conf")) {
            NgxConfig conf = NgxConfig.read(is);
            NgxParam workers = conf.findParam("worker_processes");       // Ex.1
            System.out.println(workers.getValue());

            NgxParam listen = conf.findParam("http", "server", "listen"); // Ex.2
            System.out.println(listen.getValue());

            NgxParam access_log = conf.findParam("http", "server", "access_log"); // Ex.2
            System.out.println(access_log.getValue());

            System.out.println("--------------------");

            NgxBlock locations = conf.findBlock("http", "server", "location");
            Collection<NgxEntry> entries = locations.getEntries();
            for (NgxEntry entry : entries) {
                System.out.println(entry);
            }

            System.out.println("======================");

            List<NgxEntry> ens = conf.findAll(NgxConfig.BLOCK, "http", "server", "location");
            for (NgxEntry en : ens) {
                NgxBlock location = (NgxBlock) en;
                System.out.println(">>1 "+ location.getName());
                System.out.println(">>2 "+ location.getValues());
                System.out.println(">>3 "+ location.getTokens());
                location.getEntries().forEach(len -> {
                    if (!(len instanceof NgxComment)) {
                        if (len instanceof NgxParam) {
                            NgxParam p = (NgxParam) len;
                            System.out.println(p.getName() + " = "+ p.getValue());
                        }
                    }
                });
                System.out.println("-----------------");
            }

        }
    }
}
