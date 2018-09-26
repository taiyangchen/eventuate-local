package io.eventuate.local.unified.cdc.pipeline.dblog.postgreswal.configuration;

import io.eventuate.local.common.BinlogEntryToPublishedEventConverter;
import io.eventuate.local.common.PublishedEvent;
import io.eventuate.local.common.PublishedEventPublishingStrategy;
import io.eventuate.local.common.SourceTableNameSupplier;
import io.eventuate.local.db.log.common.DbLogBasedCdcDataPublisher;
import io.eventuate.local.db.log.common.PublishingFilter;
import io.eventuate.local.java.common.broker.DataProducerFactory;
import io.eventuate.local.unified.cdc.pipeline.common.BinlogEntryReaderProvider;
import io.eventuate.local.unified.cdc.pipeline.common.factory.CdcPipelineFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresWalCdcPipelineFactoryConfiguration {
  @Bean("eventuateLocalPostgresWalCdcPipelineFactory")
  public CdcPipelineFactory<PublishedEvent> postgresWalCdcPipelineFactory(DataProducerFactory dataProducerFactory,
                                                                          PublishingFilter publishingFilter,
                                                                          BinlogEntryReaderProvider binlogEntryReaderProvider) {
    return new CdcPipelineFactory<>("eventuate-local",
            "postgres-wal",
            binlogEntryReaderProvider,
            new DbLogBasedCdcDataPublisher<>(dataProducerFactory,
                    publishingFilter,
                    new PublishedEventPublishingStrategy()),
            sourceTableName -> new SourceTableNameSupplier(sourceTableName, "events", "event_id", "published"),
            new BinlogEntryToPublishedEventConverter());
  }
}