# Структура хранения событий

## Мотивация

Для оптимизации хранения и обработки исторических событий нам необходима
структура данных, которая оптимизирована под наши сценарии использования. 

Структуры данных из стандартной и 3d party библиотек либо слишком избыточны,
либо не оптимальны на наших сценариях.

## Описание

Структура события:
```
{
  id : UUID
  timestamp : long
  value: byte[]
}
```

События хранятся в хронологическом порядке.


Сценарии использования:

- Вставка нового события. 
  Оптимистичный сценарий: в структуре есть свободные слоты, вставляемое событие
  имеет временную метку больше, чем хранимые. В этом случае вставка проходит за
  O(1). Если свободных слотов нет, мы расширяем массив на величину `FREE_SLOTS`, 
  по умолчанию 4. Если вставляемое событие имеет временную метку меньше, чем 
  временная метка последнего элемента набора – вставка проходит за O(N).

- Удаление события по id. В случае если событие было признано нелегитимным, то его
допускается удалить. Это редкий кейс, удаление проходит за O(N).
  
- Фильтрация событий по timestamp
  
- Итерация по value. Из-за колоночной структуры данных мы итерируемся по одному 
из полей без накладных расходов
  
