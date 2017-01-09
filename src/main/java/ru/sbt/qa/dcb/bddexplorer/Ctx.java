package ru.sbt.qa.dcb.bddexplorer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * Created by SBT-Razuvaev-SV on 27.12.2016.
 */
public final class Ctx {
    private static final Logger log = LoggerFactory.getLogger(Ctx.class);

    private static GenericXmlApplicationContext ctx;

    private Ctx() {}

    static void init() {
        ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:application-context.xml");
        ctx.refresh();
        log.debug("Application context loaded successfully");
    }

    static void close() {
        ctx.close();
    }

    public static ApplicationContext get() {
        return ctx;
    }

}
