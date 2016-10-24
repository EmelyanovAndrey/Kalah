package main.java.model;


/**
 * Класс Лунки
 */
public class Alveolus
{

    /**
     * Количество камней в лунке
     */
    private int quantityStones;


    //region Конструкторы
    public Alveolus()
    {
    }

    public Alveolus(int quantityStones)
    {
        this.quantityStones = quantityStones;
    }
    //endregion


    //region Методы доступа
    public int getQuantityStones()
    {
        return quantityStones;
    }

    public void setQuantityStones(int quantityStones)
    {
        this.quantityStones = quantityStones;
    }
    //endregion
}
