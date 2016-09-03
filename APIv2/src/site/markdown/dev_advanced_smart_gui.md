# Minigames-Lib 2.0 - Development

## Smart GUI

Minigames lib allowes you to create an easy inventory GUI and open it.

To use a GUI first create an enumeration that implements `ClickGuiId`.

Within your provider you will have to return that enumeration:

    Iterable<Class<? extends ClickGuiId>> getGuiIds()
    {
        final List<Class<? extends ClickGuiId>> result = new ArrayList<>();
        result.add(MyGuiIds.class);
        return result;
    }

### Implementing ClickGuiInterface

At second step create a class that implements the ClickGuiInterface. It is a simple wrapper to
return the GuiID, the line count you need and the first initial page to be displayed for
users.

### Implementing ClickGuiPageInterface

Pages are made of a localized name and an array of clickable gui items. That's it.
The minigames lib will simply display the items and invoke the action handler
on the clickable items.

### Opening the GUI

Opening the GUI is very easy.

    ArenaPlayerInterface.openGui(yourGuiInterface);

### Closing the GUI.

Fetch the gui session and invoke close.

    ArenaPlayerInterface.getGuiSession().close();

### Changing GUI items.

Do not close or re-open the GUI. Instead you can use the method setNewPage in the GuiSession.

You can even invoke the setNewPage with the same page object. It will re-load the items.

### Advanced topics

TODO Query text etc.
