# spring-boot-vaadin-hybrid-menu

This is an example integration of the HybridMenu UI component add-on for Vaadin 8 based on Spring Boot. 
Now with full SpringNavigator support!

## Adding items to the LeftMenu
To add an item to the LeftMenu use the "buildLeftMenu(...)" method in "VaadinHybridMenuUI" class.

## Adding items to the TopMenu
To add an item to the TopMenu use the "buildTopMenu(...)" method in "VaadinHybridMenuUI" class.

## Adding own views
To add your own view, create a new class (or copy a existing one from de.vidar.example.view), add the "@SpringView" annotation (like in the example views. You can see why this is important in the Notice of this README) and you are set. To navigate to a view from one of the menus use ".withNavigateTo(YourViewClass.class)" for the LeftMenu or ".setNavigateTo(YourViewClass.class)" for the TopMenu.

## Licenses
### Spring Boot 
[Spring Boot]() is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

### Vaadin
[Vaadin](https://vaadin.com) is built on open source and the liberal [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

### HybridMenu
[HybridMenu](https://vaadin.com/directory/component/HybridMenu) is a UI component add-on for Vaadin 8 written by KaesDingeling. It is distributed under [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html). For more information see [HybridMenu Github](https://github.com/KaesDingeling/Hybrid-Menu).