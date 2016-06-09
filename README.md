# CUBA Platform Translations

This repository contains third-party translations for the <a href="https://www.cuba-platform.com" target="_blank">CUBA platform</a> screens and common UI components, along with localization tools, which provide the facilities for batch localization. 

Translations are stored as sets of `messages.properties` files separated by language and platform version under the `content` directory. Each language/version directory contains subdirectories corresponding to the platform base projects (**cuba**, **reports**, etc.).

You can use the repository contents in the following ways:

- Use the provided translations as is or modify them in your project.
- Create a translation to another language.

## Using an Existing Translation

- Clone the repository or download as ZIP file.
- Go to `contents` directory and find a translation for your language and platform version.
- Copy `modules` directory for each used base project to your project root directory. For example, if your project is based on **cuba** and **reports** of the platform version 6.0.2, and you want to use the German translation, copy `content/de/6_0/cuba/modules` and `content/de/6_0/reports/modules` directories.
- Add the new locale to the _Project Properties_ > _Available locales_ list in Studio, for example `Deutsch|de`.
- Restart your application server.

To modify the translation in your project, edit the `messages_*.properties` files located inside `com.haulmont` package. If you think that some translation is incorrect, please send us your variant (as a pull request or individual files), and we will include it in the repository.

## Creating a New Translation

If the repository does not contain a translation to your language, you can make it yourself. There are two levels of translation: 

- Common components. In this case you will get translated all components of the platform, that are used in your application screens (Filter, Table elements, etc.). The platform screens and entities (like Users and Roles) will remain in English.
- Everything. All platform components and screens will become translated.

### Common Components Translation

To localize the common components, it is sufficient to translate only the _main message packs_ of the **cuba** base project:

- Copy the following English files from `content/en/{version}/cuba/modules` directory to your project and rename them for the required locale (`zz` in this example):
	- `gui/src/com/haulmont/cuba/gui/messages.properties` to `{project_root}/modules/gui/src/com/haulmont/cuba/gui/messages_zz.properties`
	- `web/src/com/haulmont/cuba/web/messages.properties` to `{project_root}/modules/web/src/com/haulmont/cuba/web/messages_zz.properties`
	- `desktop/src/com/haulmont/cuba/desktop/messages.properties` to `{project_root}/modules/desktop/src/com/haulmont/cuba/desktop/messages_zz.properties` (only if you use the **desktop** module)
- Translate messages in the copied files.
- Add the new locale to the _Project Properties_ > _Available locales_ list, for example `Zzzzzz|zz`.
- Restart your application server.

### Full Translation

For full localization, you can copy all message files from the English locale and translate them directly, or use our **mp2xls** tool. This command line tool allows you to convert message files into XLS format, translate the messages in Microsoft Excel or Open Office, and then convert the messages back into `messages.properties` format.

If you want to translate all messages in XLS, do the following:

- Build **mp2xls**:
	- Open the command line and go to `tools/mp2xls` directory.
	- Run `gradlew assemble`.
	- Unzip the contents of `tools/mp2xls/build/install/mp2xls-*.zip` archive to a local directory.

- Read English messages into the XLS file. In the command line, go to the `bin` subdirectory of the directory where you unpacked **mp2xls** on the previous step and run the following command:
	
		mp2xls -r -d {repo_path}/content/en/{version}/cuba -f {xls_path}/cuba.xls -l en
	
	Here and below `{repo_path}` is the absolute path to the root of the cloned or unzipped translation repository, `{xls_path}` is the absolute path to the directory where the XLS file will be created.

	Example for Windows:
		
		c:
		mkdir \work\xls
		mp2xls -r -d c:\work\platform-translations\content\en\6_0\cuba -f c:\work\xls\cuba.xls -l en

- Open the XLS file and add a new column for your language next to the `default` column. Give it a title denoting the language, for example `zz`.

- Translate messages in this column into your language. Leave hidden rows which are marked red as they are.

- Write translated messages back into files:
	
		mp2xls -w -d {repo_path}/content/zz/{version}/cuba -f {xls_path}/cuba.xls -l zz

	Example for Windows:
		
		c:
		mkdir \work\platform-translations\content\zz\6_0\cuba
		mp2xls -w -d c:\work\platform-translations\content\zz\6_0\cuba -f c:\work\xls\cuba.xls -l zz
		
Now you have the set of `messages_zz.properties` files which you can copy into your project as explained above.

We would appreciate it if you could send us your translations to include in the repository!

## Credits

The German translation is provided by <a href="http://www.road-to-cuba-and-beyond.com/" target="_blank">Mario David</a>.

The Spanish translation is provided by Mario Alberto Medina Rojas.
