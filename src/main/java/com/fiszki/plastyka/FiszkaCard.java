package com.fiszki.plastyka;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "fiszkaCard")
public class FiszkaCard
{
    @Element
    long id;
    @Element
    String imagePath;
    @Element
    String name;
    @Element
    String author;
    @Element
    String style;
    @Element
    String period;
}
