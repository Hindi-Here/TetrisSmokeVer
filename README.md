# TetrisSmoke

Типичная реализация культовой игры "тетрис" для мобильных android устройств.

**Дизайн игры:**

<img src="https://github.com/user-attachments/assets/dd05781b-4308-4fd5-8956-033603d7cf86" width="250" height="550">
<img src="https://github.com/user-attachments/assets/b6a45ce6-be04-4268-a925-c1dd37e2acf5" width="250" height="550">

Была предпринята попытка создать эффект дымки путем наложения нескольких полупрозрачных белых слоев для светлой темы, и добавления слоя-тумана для темной.
Изменение настроек темы возможно в классе `Theme.kt`

**_Ориентация в игре не изменяется / размер поля по Y зависит от высоты экрана_**

## Взаимодействие с фигурами

Для перемещения генерируемых фигур **проведите пальцем по экрану** влево/вправо для передвижения фигур по оси X. Для переворота фигуры просто **тапните** по экрану.
Изменить поведение можно в `setMotionListener` в классе `FigureGameplay.kt`, который отслеживает действие игрока.

```Kotlin
@SuppressLint("ClickableViewAccessibility")
    fun setMotionListener() {
        var x = 0f
        var isDragging = false

        tableLayout.setOnTouchListener { _, event ->
            when (event.action)
            {
                MotionEvent.ACTION_DOWN -> { // пользователь зажал палец
                    x = event.x
                    isDragging = false
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> { // пользователь провел пальцем влево/вправо
                    val lengthX = event.x - x

                    if (!isDragging && Math.abs(lengthX) > 0)
                    {
                        isDragging = true
                        if (lengthX > 0)
                            moveFigure(1) // смещение фигуры на 1 ячейку вправо
                        else
                            moveFigure(-1) // смещение фигуры на 1 ячейку влево

                        return@setOnTouchListener true
                    }
                }
                MotionEvent.ACTION_UP -> { // пользователь отпустил палец
                    if (!isDragging)
                        rotateFigure() // поворот фигуры
                }
            }
            false
        }
    }
```

Анимация объектов ускоряется в **два раза** после достижения порога **1000** очков. Таким образом, _до 1000 = 200мс, до 2000 = 100 мс, до 3000 = 50мс, ..._, и так далее.
Изменение порога и скорости анимации осуществляется в том же классе, в методе `animationDelay`

```Kotlin
    private fun animationDelay(): Long {
        var delay = 200L // величина в мс
        var score = 1000 // порог очков
        while  (figureScore.getScore() >= score)
        {
            delay /= 2 // сокращение анимации в 2 раза
            score += 1000 // обновление порога
        }
        return delay
    }
```

## Генерация фигур:

Фигуры генерируются в методе `gemerationFigure` класса `FigureGameplay.kt`. Для генерации фигуры берутся свойства из `FigurePropetry.kt`.
```Kotlin
class FigurePropetry {
    companion object {
        val figures: List<List<Pair<Int, Int>>> = listOf(
            listOf(Pair(0, 0)), // одна ячейка
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)), // линия из трех ячеек
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)), // квадрат
            // другие типы фигур
        )

        val colors: List<Int> = listOf(
            // список цветов
        )

        fun shadows(figureColor: Int): LayerDrawable {
            // настройка теней фигур
        }

    }
}
```

## Сохранение результатов:

В игре **рекорд сохраняется в двух ситуациях**:

А) При срабатывании game over.

Б) При очистке всего поля (первая кнопка в шапке приложения).

Реализация **механизма сохранения** осуществляется через `SharedPreferences`, хранящий значения в виде `ключ-значение`.

```Kotlin
    private fun loadScore() {
        val sharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        _bestScore = sharedPreferences.getInt("best_score", 0)
    }

    fun saveBestScore(score: Int) {
        val sharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("best_score", score)
            apply()
        }
        _bestScore = _score
        updateBestScore()
    }
```
