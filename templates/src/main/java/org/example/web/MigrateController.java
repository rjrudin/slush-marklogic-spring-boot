package org.example.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.marklogic.client.helper.DatabaseClientConfig;
import com.marklogic.client.helper.LoggingObject;
import com.marklogic.migration.sql.SqlMigrator;

/**
 * Controller for invoking the SqlMigrator program in marklogic-spring-batch.
 */
@Controller
public class MigrateController extends LoggingObject {

    @Autowired
    private DatabaseClientConfig config;

    @RequestMapping(value = "/v1/migrate", method = RequestMethod.PUT)
    public ResponseEntity<?> upload(@RequestBody MigrateData data) throws Exception {
        List<String> args = new ArrayList<>();
        args.add("-sql");
        args.add(data.getSql());
        args.add("-rootLocalName");
        args.add(data.getRootLocalName());

        args.add("-host");
        args.add(config.getHost());
        args.add("-port");
        args.add(config.getPort() + "");
        args.add("-username");
        args.add(config.getUsername());

        String db = config.getDatabase();
        if (StringUtils.hasText(db)) {
            args.add("-database");
            args.add(db);
        }

        args.add("-jdbcDriver");
        args.add(data.getJdbcDriver());
        args.add("-jdbcUrl");
        args.add(data.getJdbcUrl());
        args.add("-jdbcUsername");
        args.add(data.getJdbcUsername());

        logger.info("Launching migrator with args (excluding passwords): " + args);

        args.add("-password");
        args.add(config.getPassword());
        args.add("-jdbcPassword");
        args.add(data.getJdbcPassword());

        SqlMigrator.main(args.toArray(new String[] {}));
        return null;
    }
}
