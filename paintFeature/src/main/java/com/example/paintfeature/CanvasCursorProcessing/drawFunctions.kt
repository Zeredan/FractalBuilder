package com.example.paintfeature.CanvasCursorProcessing

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set

fun drawBresenhamLine (xstart : Int, ystart: Int, xend: Int, yend: Int, color: Int, result: Bitmap)
{
    fun sign (x: Int) : Int{
        return when{
            x > 0 -> 1
            x == 0 -> 0
            else -> -1
        }
    }

    var (x, y, dx, dy, incx) = listOf(0,0,0,0,0)
    var (incy, pdx, pdy, es, el) = listOf(0,0,0,0,0)
    var err = 0

    dx = xend - xstart;//проекция на ось икс
    dy = yend - ystart;//проекция на ось игрек

    incx = sign(dx);
    /*
     * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
     * справа налево по иксу, то incx будет равен -1.
     * Это будет использоваться в цикле постороения.
     */
    incy = sign(dy);
    /*
     * Аналогично. Если рисуем отрезок снизу вверх -
     * это будет отрицательный сдвиг для y (иначе - положительный).
     */

    if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
    if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
    //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

    if (dx > dy)
    //определяем наклон отрезка:
    {
        /*
         * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
         * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
         * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
         * по y сдвиг такой отсутствует.
         */
        pdx = incx;	pdy = 0;
        es = dy;	el = dx;
    }
    else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
    {
        pdx = 0;	pdy = incy;
        es = dx;	el = dy;//тогда в цикле будем двигаться по y
    }

    x = xstart;
    y = ystart;
    err = el/2;
    try{
        result[x,y] = color
    }
    catch (e: Exception){

    }
    //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

    (0 until el).forEach {
        err -= es;
        if (err < 0)
        {
            err += el;
            x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
            y += incy;//или сместить влево-вправо, если цикл проходит по y
        }
        else
        {
            x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
            y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
        }

        try{
            result[x,y] = color
        }
        catch (e: Exception){

        }
    }
}