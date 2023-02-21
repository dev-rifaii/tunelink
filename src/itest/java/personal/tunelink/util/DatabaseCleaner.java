package personal.tunelink.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestComponent
public class DatabaseCleaner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String TRUNCATE_ALL_EXCEPT =
            """
                    DO $$
                    DECLARE row RECORD;
                    BEGIN
                      FOR row IN SELECT table_name
                        FROM information_schema.tables
                        WHERE table_type='BASE TABLE'
                        AND table_schema='public'
                        AND table_name NOT IN ('')\s
                      LOOP\s
                        EXECUTE format('TRUNCATE TABLE public.%I CASCADE;', row.table_name);
                      END LOOP;
                    END;
                    $$;
            """;

    public void truncate() {
        jdbcTemplate.execute(TRUNCATE_ALL_EXCEPT);
    }

}
