package com.kakao.tech.springaibasic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
public class SqlController {
    @Value("classpath:/schema.sql")
    private Resource ddlResource;

    @Value("classpath:/sql-prompt-template.st")
    private Resource sqlPromptTemplateResource;

    private final ChatClient aiClient;
    private final JdbcTemplate jdbcTemplate;

    public SqlController(
            ChatClient.Builder aiClientBuilder,
            JdbcTemplate jdbcTemplate) {
        this.aiClient = aiClientBuilder.build();
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping(path="/sql")
    public Answer sql(@RequestParam String question) throws IOException {
        String schema = ddlResource.getContentAsString(Charset.defaultCharset());

        String query = aiClient.prompt()
                .user(userSpec -> userSpec
                        .text(sqlPromptTemplateResource)
                        .param("question", question)
                        .param("ddl", schema)
                )
                .call()
                .content();

        if (query.toLowerCase().startsWith("select")) {
            return new Answer(query, jdbcTemplate.queryForList(query));
        }

        throw new SqlGenerationException(query);
    }
}
