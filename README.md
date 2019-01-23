# CUBA Platform Translations

This repository contains third-party translations for the <a href="https://www.cuba-platform.com" target="_blank">CUBA platform</a> screens and common UI components. 

Translations are stored as sets of language-specific `messages.properties` files separated by the platform base projects (**cuba**, **reports**, etc.) and their modules.

You can use the repository contents in the following ways:

- Use the provided translations as is or copy and modify them in your project.
- Create a translation to another language.

## Using an Existing Translation

- Clone the repository and checkout a branch corresponding to the platform version used in your project.
- Find your language in the `content` directory.
- Copy `modules` directory for each used application component to your project's root directory. For example, if your project is based on **cuba** and **reports**, and you need the German translation, copy `content/de/cuba/modules` and `content/de/reports/modules` directories. 

    If your project doesn't contain the `gui` module, copy the contents of the translation's `modules/gui` folder to your project's `web` module. For example, if you are applying German translation of the **cuba** app component, your `web` module must contain the `modules/web/src/com/haulmont/cuba/gui/messages_de.properties` file.

- Optionally, you can remove `message.properties` files with English messages from the copied directories.
- Add the new locale to the _Project Properties_ > _Available locales_ list in Studio, for example `Deutsch|de`.
- Restart your application server.

To modify the translation in your project, edit the `messages_*.properties` files located inside `com.haulmont` package. If you think that some translation is incorrect, please send us your variant as a pull request, and we will include it in the repository.

## Creating a New Translation

If the repository does not contain a translation to your language, you can make it yourself. There are two levels of translation: 

- Common components. In this case you will get translated all components of the platform, that are used in your application screens (Filter, Table elements, etc.). The platform screens and entities (like Users and Roles) will remain in English.
- Everything. All platform components and screens will become translated.

### Common Components Translation

To localize the common components, it is sufficient to translate only common messages of the **cuba** application component. These messages are located in `content/en/cuba/modules/gui/src/com/haulmont/cuba/gui/messages.properties` and `content/en/cuba/modules/web/src/com/haulmont/cuba/web/messages.properties` files with simple keys. Simple key is a key without a path part separated by "/" symbol. These keys are located in the end of the files.

- Add the new locale to the _Project Properties_ > _Available locales_ list, for example `Zzzzzz|zz`.
- Copy the common messages in English to the main message pack of your project. You can open the message pack in Studio and switch to the tab of your language.
- Translate the messages.
- Restart your application server.

### Full Translation

For full localization, copy all English message files to your project as explained above in Using an Existing Translation. Then copy existing files to `messages_zz.properties` (provided that `zz` is your language code) and translate them.
 
You can use the IntelliJ IDEA resource bundle editor (or a similar feature of another IDE) to identify messages missing in your language.

We would appreciate it if you could send us your translations to include in the repository!

## Credits

The German translation is provided by <a href="http://www.road-to-cuba-and-beyond.com/" target="_blank">Mario David</a> and Matthias Hamann.

The Spanish translation is provided by Mario Alberto Medina Rojas and Alfredo Punzi.

The Danish translation is provided by Torben Merrald.

The Dutch translation is provided by Berend Tel.

The Portuguese translation is provided by Pedro Armelim.

The Brazilian Portuguese translation is provided by Peterson Machado.

The Simplified Chinese translation is provided by GUORUI LV.

The Romanian translation is provided by Sorin Federiga.

The Italian translation is provided by Paolo Furini.

The French translation is provided by Olga Shiryaeva.
