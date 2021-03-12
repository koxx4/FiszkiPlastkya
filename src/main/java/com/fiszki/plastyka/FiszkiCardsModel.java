package com.fiszki.plastyka;

import javafx.scene.image.Image;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FiszkiCardsModel
{
    static final public String IMAGES_PATH = "Data/images/";
    static final public String CARDS_XML_PATH = "Data/info/";
    static final private String[] restrictedCharacters = {"?", ">", "<", ":", "\"", "\\", "/", "|", "*"};
    public static Random randomGenerator;

    static private List<FiszkaCard> loadedCards;
    static private List<FiszkaCard> addedCards;
    static private List<String> IDs;

    static private FiszkaCard activeCard;
    static private boolean initialized = false;

    static public FiszkaCard getNextCard()
    {
        //If we are below loadedImages limit
        if(loadedCards.indexOf(activeCard) - 1 > loadedCards.size())
        {
            activeCard = loadedCards.get(loadedCards.indexOf(activeCard)+1);
        }

        return activeCard;
    }
    static public FiszkaCard getPreviousCard()
    {
        //If we are below or at 0 index (which is the last)
        if(loadedCards.indexOf(activeCard) > 0)
        {
            activeCard = loadedCards.get(loadedCards.indexOf(activeCard)-1);
        }

        return activeCard;
    }
    static public FiszkaCard getRandomCard()
    {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(loadedCards.size());

        while (randomIndex == loadedCards.indexOf(activeCard))
        {
            randomIndex = randomGenerator.nextInt(loadedCards.size());
        }

        activeCard = loadedCards.get(randomIndex);
        return activeCard;
    }
    static public Image getCardImage(FiszkaCard card)
    {

        File dir = new File(IMAGES_PATH);
        File[] files = dir.listFiles((dir1, name) -> {
            return name.startsWith("image_"+ card.id) && (name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("png"));
        });

        URL imagePath = null;
        try
        {
            imagePath = files[0].toURI().toURL();
        } catch (Exception e)
        {
            //If there was any kind of problem retreiving an image just punt a blank image
            return getBlankImage();
        }

        return new Image(String.valueOf(imagePath));
    }
    static public Image getActiveCardImage()
    {

        File dir = new File(IMAGES_PATH);
        File[] files = dir.listFiles((dir1, name) -> {
            return name.startsWith("image_"+ activeCard.id) && (name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("png"));
        });

        String imagePath = null;
        try
        {
            imagePath = files[0].toString();
        } catch (Exception e)
        {
            //If there was any kind of problem retreiving an image just punt a blank image
            return getBlankImage();
        }

        return new Image(imagePath);
    }
    static public Image getBlankImage()
    {
        Image placeholder = new Image(String.valueOf(FiszkiCardsModel.class.getResource("/placeholder_image.png")));
        return placeholder;
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
    static public void downloadCardsImages(IDownloadingEvent downloadingEventHandler)
    {
        try
        {
            for (var card : loadedCards)
            {
                String imageExtension = card.imageURL.substring(card.imageURL.length()-4);
                String imageFileName = "image_" + card.id + imageExtension;
                downloadingEventHandler.onDownloadingAction(imageFileName, (float) loadedCards.indexOf(card)/ (loadedCards.size()-1));
                MaterialDownloader.downloadNextCardImage(card, IMAGES_PATH, imageFileName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        activeCard = loadedCards.get(0);
        initialized = true;
    }
    static public void initializeModel() throws NoCardsLoadedException
    {
        if(!initialized)
        {
            loadedCards = new ArrayList<>();
            addedCards = new ArrayList<>();
            IDs = new ArrayList<>();

            randomGenerator = new Random();
            loadCardsData();

            if (loadedCards.isEmpty())
                throw new NoCardsLoadedException("Couldn't load any cards!");

            activeCard = loadedCards.get(randomGenerator.nextInt(loadedCards.size()));
            initialized = true;
        }
    }
    static public void closeModel()
    {
        System.out.println("Closing model");
        try
        {
            saveAddedOrModifiedCards();
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
    static public void downloadOnlineCards(IDownloadingEvent downloadingEventHandler) throws Exception
    {
        System.out.println("Downlading cards data from " + MaterialDownloader.ART_PAGE_URL);
        List<FiszkaCard> downloadedCards = MaterialDownloader.connectAndParseHTML(downloadingEventHandler);
        Serializer serializer = initializeSimpleSerializer();
        for (FiszkaCard card : downloadedCards)
        {
            try
            {
                String filePath = CARDS_XML_PATH + "card_" +  card.id + ".xml";
                File xmlCardFile = new File(filePath);
                serializer.write(card, new FileWriter(xmlCardFile, StandardCharsets.UTF_8));
            } catch (Exception e)
            {
                e.printStackTrace(System.out);
            }
        }
    }
    static public void saveAddedOrModifiedCards()
    {
        if(!addedCards.isEmpty())
        {
            Serializer serializer = initializeSimpleSerializer();
            for (FiszkaCard card : addedCards)
            {
                try
                {
                    String filePath = CARDS_XML_PATH + "card_" +  card.id + ".xml";
                    File xmlCardFile = new File(filePath);
                    serializer.write(card, new FileWriter(xmlCardFile, StandardCharsets.UTF_8));
                } catch (Exception e)
                {
                    e.printStackTrace(System.out);
                }
            }
            addedCards.clear();
        }
    }
    static public void makeDataDirs()
    {
        File imagesDir = new File(IMAGES_PATH);
        if(!imagesDir.exists())
            imagesDir.mkdirs();

        File dataDir = new File(CARDS_XML_PATH);
        if(!dataDir.exists())
            dataDir.mkdirs();
    }
    static public void addNewCard(FiszkaCard newCard)
    {
        loadedCards.add(newCard);
        addedCards.add(newCard);
        IDs.add(newCard.id);
    }
    static public boolean isValidID(String id)
    {
        return !IDs.contains(id) && !Arrays.stream(restrictedCharacters).anyMatch(id::contains);
    }
    static private void loadCardsData()
    {

            File cardsDirectory = new File(CARDS_XML_PATH);
            File[] cardsFiles = cardsDirectory.listFiles((file, fileName) -> {
                return fileName.startsWith("card") && fileName.endsWith(".xml");
            });

            Serializer serializer = initializeSimpleSerializer();
            for( File cardFile : cardsFiles)
            {
                try
                {
                    FiszkaCard loadedCard = serializer.read(FiszkaCard.class,
                            new FileReader( cardFile, StandardCharsets.UTF_8));
                    loadedCards.add(loadedCard);
                    IDs.add(loadedCard.id);
                }
                catch (Exception e)
                {
                    e.printStackTrace(System.out);
                }
            }
    }
    static private Serializer initializeSimpleSerializer()
    {
        Serializer serializer = new Persister();
        return serializer;
    }
}
