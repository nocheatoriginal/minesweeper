public enum Tiles
{
    OPEN("open.png"),
    CLOSED("closed.png"),
    FLAG("flag.png"),
    WRONGFLAG("wrongflag.png"),
    BOMB("bomb.png"),
    BOMBSELECTED("bombSelected.png"),
    WRONGBOMB("wrongbomb.png"),
    ONE("1.png"),
    TWO("2.png"),
    THREE("3.png"),
    FOUR("4.png"),
    FIVE("5.png"),
    SIX("6.png"),
    SEVEN("7.png"),
    EIGHT("8.png"),
    START("start.png");

    private final Sprite sprite;
    Tiles(String path)
    {
        sprite = new Sprite("/sprites/" + path);
    }

    public Sprite getSprite()
    {
        return sprite;
    }
}
