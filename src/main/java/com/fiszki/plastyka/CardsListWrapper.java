package com.fiszki.plastyka;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class CardsListWrapper
{
    @ElementList
    private List<FiszkaCard> cards;

    List<FiszkaCard> getCards()
    {
        return this.cards;
    }

    void setCards(List<FiszkaCard> cards)
    {
        this.cards = cards;
    }
}
