package ru.otus.homework.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.api.SensorDataProcessor;
import ru.otus.homework.api.model.SensorData;
import ru.otus.homework.lib.SensorDataBufferedWriter;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private Set<SensorData> bufferedData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        bufferedData = new ConcurrentSkipListSet<>(Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        bufferedData.add(data);

        if (bufferedData.size() >= bufferSize) {
            flush();
        }
    }

    public synchronized void flush() {
        try {
            if (!bufferedData.isEmpty()) {
                writer.writeBufferedData(bufferedData.stream().toList());
                bufferedData.clear();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
