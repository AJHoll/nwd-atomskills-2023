# Environment API

## Заявки

---

### GET `/crm/requests`

---

#### Описание

```
Возвращает все заявки
```

#### Возвращаемые данные

```typescript
{
  id: number,
  number: string,
  date: Date,
  contractor: {
    id: number,
    inn: string,
    caption: string
  }
  description: string,
  state: {
    code: string,
    caption: string
  }
  releaseDate: Date
}[]
```

---

### GET `/crm/requests/active`

---

#### Описание

```
Возвращает все активные заявки (в состояниях Формируется и В производстве)
```

#### Возвращаемые данные

```typescript
{
  id: number,
  number: string,
  date: Date,
  contractor: {
    id: number,
    inn: string,
    caption: string
  }
  description: string,
  state: {
    code: string,
    caption: string
  }
  releaseDate: Date
}[]
```

---

### GET `/crm/requests/{requestId}`

---

#### Описание

```
Возвращает заявку по id
```

#### Параметры

- **requestId: Long** - идентификатор заявки

#### Возвращаемые данные

```typescript
{
  id: number,
  number: string,
  date: Date,
  contractor: {
    id: number,
    inn: string,
    caption: string
  }
  description: string,
  state: {
    code: string,
    caption: string
  }
  releaseDate: Date
}
```

---

### GET `/crm/requests/{requestId}/items`

---

#### Описание

```
Возвращает позиции заявки по id
```

#### Параметры

- **requestId: Long** - идентификатор заявки

#### Возвращаемые данные

```typescript
{
  id: number,
  request: {
    id: number,
    number: string,
    date: Date,
    contractor: {
      id: number,
      inn: string,
      caption: string
    }
    description: string,
    state: {
      code: string,
      caption: string
    }
    releaseDate: Date
  },
  product: {
    id: number,
    code: string,
    caption: string,
    millingTime: number,
    latheTime: number
  }
  ,quantity: number,
  ,quantityExec: number
}[]
```

---

### GET `/crm/requests/{requestId}/items/{itemId}`

---

#### Описание

```
Возвращает позицию заявки по id заявки и id позиции
```

#### Параметры

- **requestId: Long** - идентификатор заявки
- **itemId: Long** - идентификатор позиции заявки

#### Возвращаемые данные

```typescript
{
  id: number,
  request: {
    id: number,
    number: string,
    date: Date,
    contractor: {
      id: number,
      inn: string,
      caption: string
    }
    description: string,
    state: {
      code: string,
      caption: string
    }
    releaseDate: Date
  },
  product: {
    id: number,
    code: string,
    caption: string,
    millingTime: number,
    latheTime: number
  }
  ,quantity: number,
  ,quantityExec: number
}
```

---

### POST `/crm/requests/{requestId}/set-state/in-production`

---

#### Описание

```
Устанавливает состояние заявки в "В производстве"
```

#### Параметры

- **requestId: Long** - идентификатор заявки

---

### POST `/crm/requests/{requestId}/set-state/closed`

---

#### Описание

```
Устанавливает состояние заявки в "Закрыто"
```

#### Параметры

- **requestId: Long** - идентификатор заявки

---

### PUT `/crm/requests/{requestId}/items/{itemId}/add-execution-qty/{addingQuantity}`

---

#### Описание

```
Добавляет количество исполненной продукции позиции заявки по id заявки и id позиции заявки
```

#### Параметры

- **requestId: Long** - идентификатор заявки
- **itemId: Long** - идентификатор позиции заявки
- **addingQuantity: Long** - Добавляемое количество исполнения

#### Возвращаемые данные

```typescript
{
  id: number,
  request: {
    id: number,
    number: string,
    date: Date,
    contractor: {
      id: number,
      inn: string,
      caption: string
    }
    description: string,
    state: {
      code: string,
      caption: string
    }
    releaseDate: Date
  },
  product: {
    id: number,
    code: string,
    caption: string,
    millingTime: number,
    latheTime: number
  }
  ,quantity: number,
  ,quantityExec: number
}
```

---

## Справочники

---

### GET `/dict/contractors`

---

#### Описание

```
Возвращает всех контрагентов системы
```

#### Возвращаемые данные

```typescript
{
  id: number,
  inn: string,
  caption: string
}[]
```

---

### GET `/dict/contractors/{contractorId}`

---

#### Описание

```
Возвращает контрагента по id
```

#### Параметры

- **contractorId: Long** - идентификатор контрагента

#### Возвращаемые данные

```typescript
{
  id: number,
  inn: string,
  caption: string
}[]
```

---

### GET `/dict/products`

---

#### Описание

```
Возвращает все изделия ГП
```

#### Возвращаемые данные

```typescript
{
  id: number,
  code: string,
  caption: string,
  millingTime: number,
  latheTime: number
}[]
```

---

### GET `/dict/products/{productId}`

---

#### Описание

```
Возвращает изделие ГП по id
```

#### Параметры

- **productId: Long** - идентификатор изделия ГП

#### Возвращаемые данные

```typescript
{
  id: number,
  code: string,
  caption: string,
  millingTime: number,
  latheTime: number
}[]
```

---

## Станки

---

### GET `/mnf/machines`

---

#### Описание

```
Возвращает фрезерные и токарные станки в формате

'Имя станка': Порт для обращения
```

#### Возвращаемые данные

```typescript
{
  lathe: {[key: string]: number},
  milling: {[key: string]: number}
}
```

#### Пример

```typescript
{
  lathe: {'lm-1' : 1041, 'lm-2' : 1042},
  milling: {'mm-1' : 1051, 'mm-2' : 1053}
}
```

---

## API Станков

Важно помнить, что каждый станок имеет свой порт, как пример, по-умолчанию станок **lm-1** имеет порт **1041** и запрос к его статусу будет следующим, если вы запрашиваете его с **localhost**: `http://localhost:1041/status`.
Все дальнейшие урлы будут написаны в формате, начиная с порта, например: `:{port}/status`.

---

### GET `:{port}/status/all`

---

#### Описание

```
Возвращает все состояния станка на порту {port}
```

#### Параметры

- **port: Long** - порт станка

#### Возвращаемые данные

```typescript
{
  id: number,
  code: string,
  state: {
    code: string,
    caption: string
  },
  advInfo: Object, // В это поле можно будет писать любой json объект
  beginDateTime: Date,
  endDateTime: Date
}[]
```

---

### GET `:{port}/status`

---

#### Описание

```
Возвращает текущее состояние станка на порту {port}
```

#### Параметры

- **port: Long** - порт станка

#### Возвращаемые данные

```typescript
{
  id: number,
  code: string,
  state: {
    code: string,
    caption: string
  },
  advInfo: Object, // В это поле можно будет писать любой json объект
  beginDateTime: Date,
  endDateTime: Date
}
```

---

### POST `:{port}/set/waiting`

---

#### Описание

```
Переводит состояние станка на порту {port} в состояние "Ожидание"
```

#### Параметры

- **port: Long** - порт станка
- **advInfo: Object** - можно записывать любой json объект, если нужно что то передать станку и получить после. Параметр передается в теле запроса

---

### POST `:{port}/set/working`

---

#### Описание

```
Переводит состояние станка на порту {port} в состояние "Используется"
```

#### Параметры

- **port: Long** - порт станка
- **advInfo: Object** - В передаваемом объекте обязательно должно быть поле **productId** в которое нужно передать, какую ГП вы производите, в противном случае вы получите ошибку. Параметр передается в теле запроса

---

### POST `:{port}/set/broken`

---

#### Описание

```
Переводит состояние станка на порту {port} в состояние "Сломан". Использовать, если вам недостаточно автоматического ломателя станков.
```

#### Параметры

- **port: Long** - порт станка
- **advInfo: Object** - можно записывать любой json объект, если нужно что то передать станку и получить после. Параметр передается в теле запроса

---

### POST `:{port}/set/repairing`

---

#### Описание

```
Переводит состояние станка на порту {port} в состояние "На ремонте". Используется для отправки заявки на починку станка механику. Через какое то время после этого запроса - станок будет починен и переведется в ожидание
```

#### Параметры

- **port: Long** - порт станка
- **advInfo: Object** - можно записывать любой json объект, если нужно что то передать станку и получить после. Параметр передается в теле запроса

---
