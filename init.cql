-- Создание таблицы request_logs
CREATE TABLE IF NOT EXISTS my_keyspace.request_logs (
                                                        request_id UUID PRIMARY KEY,
                                                        timestamp TIMESTAMP,
                                                        user_id TEXT,
                                                        user_role TEXT,
                                                        url TEXT,
                                                        http_method TEXT,
                                                        request_parameters MAP<TEXT, TEXT>,
                                                        response_status TEXT,
                                                        server_response TEXT,
                                                        error_logs TEXT,
                                                        caching_information TEXT
);