package com.fiszki.plastyka;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class FiszkiCardsModel
{
    static final public String IMAGES_PATH = "Data/images/";
    static final public String CARDS_XML_PATH = "Data/info/";

    static private List<FiszkaCard> cards;

    static private FiszkaCard activeCard;
    static private int loadedImages = 0;
    static private boolean initialized = false;

    static public URL getNextArtImage()
    {
        //If we are below loadedImages limit
        if(cards.indexOf(activeCard) - 1 > cards.size())
        {
            activeCard = cards.get(cards.indexOf(activeCard)+1);
        }

        return getImageURL();
    }
    static public URL getPrevArtImage()
    {
        //If we are below or at 0 index (which is the last)
        if(cards.indexOf(activeCard) > 0)
        {
            activeCard = cards.get(cards.indexOf(activeCard)-1);
        }

        return getImageURL();
    }
    static public URL getRandomNextArtImage()
    {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(loadedImages);

        while (randomIndex == cards.indexOf(activeCard))
        {
            randomIndex = randomGenerator.nextInt(loadedImages);
        }

        activeCard = cards.get(randomIndex);
        return getImageURL();
    }
    static private URL getImageURL()
    {

        File dir = new File(IMAGES_PATH);
        File[] files = dir.listFiles((dir1, name) -> {
            return name.startsWith("image"+ activeCard.id) && (name.endsWith("jpg") || name.endsWith("png"));
        });

        URL returnURL = null;
        try
        {
            returnURL = files[0].toURI().toURL();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return returnURL;
    }
    static public int getImagesNumber()
    {
        //Get images number
        File dir = new File(IMAGES_PATH);
        var images = dir.listFiles((x,z) -> {
            return z.startsWith("image") && z.endsWith(".jpg");
        });

        if (images != null)
        {
            return images.length;
        }
        return 0;
    }
    static public void constructCards(IImageDownloadingEvent downloadingEventHandler)
    {
        makeDataDirs();
        try
        {
            cards = MaterialDownloader.connectAndParseHTML();
            for (var card : cards)
            {
                String imageFileName = "image" + card.id + ".jpg";
                downloadingEventHandler.onDownloadingAction(imageFileName, (float)cards.indexOf(card)/cards.size());
                MaterialDownloader.downloadNextCardImage(card, IMAGES_PATH, imageFileName);
            }
            saveXMLCardsData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        activeCard = cards.get(0);
        loadedImages = getImagesNumber();
        initialized = true;
    }
    static  private void saveXMLCardsData() throws Exception
    {
        System.out.println("Saving XML cards data...");
        File xmlFile = new File(CARDS_XML_PATH + "cards.xml");
        CardsListWrapper wrapper = new CardsListWrapper();
        wrapper.setCards(cards);
        //Serializer didn't work without passing an ownership of a cards
        cards = null;
        initializeSimpleSerializer().write( wrapper, new FileWriter(xmlFile, StandardCharsets.UTF_8));
        cards = wrapper.getCards();
    }
    static public void initializeModel()
    {
        if(!initialized)
        {
            loadCardsData();
            loadedImages = getImagesNumber();
            activeCard = cards.get(0);
            initialized = true;
        }
    }
    static private void loadCardsData()
    {
        try
        {
            File sourceXmlFile = new File(CARDS_XML_PATH + "cards.xml");
            CardsListWrapper tempWrapper = initializeSimpleSerializer().read(CardsListWrapper.class , new FileReader(sourceXmlFile, StandardCharsets.UTF_8));
            cards = tempWrapper.getCards();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    static private Serializer initializeSimpleSerializer()
    {
        Serializer serializer = new Persister();
        return serializer;
    }
    static public void closeModel()
    {
        System.out.println("Closing model");
        try
        {
            saveXMLCardsData();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed to save XML cards data!\nPossible changes hasn't been written :/");
        }
    }
    static public FiszkaCard getActiveCard()
    {
        return activeCard;
    }
    static private void makeDataDirs()
    {
        File imagesDir = new File(IMAGES_PATH);
        if(!imagesDir.exists())
            imagesDir.mkdirs();

        File dataDir = new File(CARDS_XML_PATH);
        if(!dataDir.exists())
            dataDir.mkdirs();
    }
}
