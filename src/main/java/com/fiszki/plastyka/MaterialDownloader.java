package com.fiszki.plastyka;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MaterialDownloader
{
    public static final String ART_PAGE_URL = "http://www.zsplast.gdynia.pl/historia_sztuki/";

    public static ArrayList<FiszkaCard> connectAndParseHTML(IDownloadingEvent downloadingEventHandler) throws IOException
    {
        ArrayList<FiszkaCard> cards = new ArrayList<FiszkaCard>();
        //Connects to page and fetches html doc
        Document htmlDocument;
        try
        {
            htmlDocument = Jsoup.parse(new URL(ART_PAGE_URL).openStream(), "UTF-8", ART_PAGE_URL);
        }
        catch (IOException e)
        {
            throw e;
        }

        //Page-wrapper is the div where are all of the listings
        org.jsoup.select.Elements pureElements  = htmlDocument.body().getElementById("page-wrapper").getAllElements()
                .select("div.wrapper.style2").select("h3,li");
        //Clean first 4 elements. They're not interesting to us
        for (int i = 0; i < 4; i++)
        {
            pureElements.remove(0);
        }

        AtomicReference<String> lastArtStyle = new AtomicReference<>("nieznany styl");
        AtomicLong cardNumber = new AtomicLong(0);

        pureElements.forEach( element -> {

            downloadingEventHandler.onDownloadingAction("cards.xml",
                    cardNumber.get() / (float)pureElements.size()  );

            if(element.tagName() == "h3")
            {
                lastArtStyle.set(element.ownText().toLowerCase());
            }
            else //If it's a 'li' node
            {
                element.select("a[href]").forEach( aElement -> {
                    FiszkaCard newCard = new FiszkaCard();

                    newCard.id = String.valueOf(cardNumber.get());
                    newCard.style = lastArtStyle.get();
                    newCard.style = newCard.style.replaceAll("\\p{Punct}", " ");
                    if(newCard.style.isEmpty())
                        newCard.style = "styl nieznany";

                    newCard.period = "nieznany";

                    newCard.author = element.ownText();
                    newCard.author = newCard.author.replaceAll("\\p{Punct}", " ");
                    if(newCard.author.isEmpty())
                        newCard.author = "nieznany";

                    newCard.imageURL = aElement.absUrl("href");
                    newCard.name = aElement.ownText();
                    newCard.name = newCard.name.replaceAll("\\p{Punct}", " ");
                    if(newCard.name.isEmpty())
                        newCard.name = "nieznane";

                    cards.add(newCard);
                    cardNumber.getAndIncrement();
                });
            }
        });

        return cards;
    }
    public static void downloadNextCardImage(FiszkaCard card, String savePath , String imageFileName )
    {
        //Create name for a newly downloaded image and check if threre is a photo with
        //the same name. If there is, skip this file and move on to downloading next
        File newPhotoFile = new File(savePath + imageFileName);
        if(newPhotoFile.exists() && !newPhotoFile.isDirectory())
        {
            System.out.println("Omitting " + imageFileName + ". This file already exists!");
            return;
        }
        System.out.println("Getting \"" + imageFileName + "\" image: \"" + card.name + "\".");
        boolean connectionEstablished = false;

        URL url = null;
        try
        {
            url = new URL(card.imageURL);
        }
        catch (MalformedURLException e)
        {
            System.out.println(e.getMessage());
        }

        if (url == null)
            return;

        ReadableByteChannel readableByteChannel = null;
        //Try to connect 3 times, then abort
        for (int i = 0 ; i < 3 && !connectionEstablished; i++)
        {
            try
            {
                readableByteChannel = Channels.newChannel(url.openStream());
                connectionEstablished = true;
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }

        if(readableByteChannel == null)
            return;


        FileOutputStream fileOutput = null;

        try
        {
            fileOutput = new FileOutputStream(savePath + imageFileName);
            FileChannel fileChannel = fileOutput.getChannel();

            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Downloading \""+ imageFileName + "\" failed :c");
        }
    }
}
