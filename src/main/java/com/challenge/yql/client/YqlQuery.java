package com.challenge.yql.client;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
public class YqlQuery {

    public static final String QUERY_URL_PUBLIC =
            "http://query.yahooapis.com/v1/public/yql";
    public static final String ENV = "env";
    public static final String FORMAT = "format";
    public static final String QUERY = "q";
    private final Logger logger = LoggerFactory.getLogger(YqlQuery.class);
    private final String queryString;
    private final List<String> environmentFiles = new LinkedList<String>();
    private URI compiledUri;
    private ResultFormat format;


    public YqlQuery(String queryString, Integer n, String... params) {
        Assert.isTrue(n.equals(params.length), "number of params should be the same as n");
        this.queryString = MessageFormat.format(queryString, params);
    }

    public void setFormat(ResultFormat format) {
        this.format = format;
    }

    /**
     * Returns the URI for this query.
     *
     * @return compiledUri
     */
    public URI toUri() {
        if (compiledUri == null) {
            compiledUri = compileUri();
        }
        return compiledUri;
    }

    /**
     * Returns a newly constructed URI for this query.
     *
     * @return the URI
     */
    private URI compileUri() {
        try {
            URIBuilder builder = new URIBuilder(QUERY_URL_PUBLIC);

            builder.addParameter(QUERY, queryString);

            // Set parameters that must be on a query
            environmentFiles.forEach((env) -> builder.addParameter(ENV, env));

            if (format != null) {
                builder.addParameter(FORMAT, format.getName());
            }
            logger.debug(builder.toString());
            return builder.build();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public enum ResultFormat {
        XML("xml"),
        JSON("json");

        private String name;

        ResultFormat(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
