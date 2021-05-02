package com.github.eutkin;

import java.io.Externalizable;
import java.util.UUID;

/**
 * Колоночная структура данных для хранения исторических записей.
 * <p>Каждая запись состоит не более, чем из трех колонок.</p>
 * <p>Все методы – мутирующие</p>
 *
 * @param <T>
 */
public interface Records extends Iterable<byte[]>, Externalizable {

    /**
     * Вставляет новое событие, соблюдая сортировку по timestamp.
     *
     * @param timestamp время события.
     * @param id        идентификатор события.
     * @param value     дополнительное значение, например, сумма платежа или идентификатор клиента.
     */
    void add(long timestamp, UUID id, byte[] value);

    /**
     * Удаляет все данные, старше заданного периода.
     *
     * @param timestampFrom начало периода.
     */
    void truncate(long timestampFrom);

    /**
     * Удаляет запись по идентификатору.
     *
     * @param id идентификатор события
     */
    void delete(UUID id);

    /**
     * Возвращает размер коллекции.
     *
     * @return размер коллекции.
     */
    int size();
}
