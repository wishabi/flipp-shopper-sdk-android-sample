# Flipp Shopper SDK Integration Sample
## About this Demo
This is a demo app that demonstrates the integration of Flipp Shopper SDK into a native Android app.
To try it out:
- download or checkout the code
- open in Android Studio
- sync gradle dependencies
- run the app.

---

# Integrating Flipp Shopper SDK 

## Requirements
To successfully use the SDK, you need to request the following from us:
1) Access to the SDK library (Can be downloaded from this repo)
2) **UserID**, **siteID**, **zoneID**(s) -- to initialize the SDK in runtime and start showing flyers

The rest of this guide assumes you have obtained the aforementioned keys.


## Adding Flipp SDK to your Project
1) Download latest `flipp-shopper-sdk-vX.Y.aar` from [releases page](https://github.com/wishabi/flipp-shopper-sdk-android-sample/releases) 
2) Add it to your project's `libs` folder (such as `my-project/app/libs`)
3) Make sure `libs` folder is included in project's `dependencyResolution`, i.e your project's `settings.gradle` has `libs` as a flat directory:
```groovy
dependencyResolutionManagement {
    repositories {
        // other repositories ...
        // ...
        flatDir {
            dirs 'libs'
        }
    }
}
```
4) In your app's `build.gradle` dependencies, add `implementation fileTree(dir: 'libs', includes: ['*.aar'])`

That's it! Now you have the Flipp Shopper SDK added and ready to use.

## Using the SDK
### Initializing the SDK
You need to initialize the SDK at runtime by calling `Flipp.init` as soon as possible, preferably from a subclass of `Application`
or from your `MainActivity`:

```kotlin
Flipp.init(application, siteID, userID, isDebug, listOf(zoneIDs)) {
    when (it) {
        Flipp.SdkInitCallback.SdkInitResult.OK ->
            Log.d("MyApp", "SDK initialized")
        Flipp.SdkInitCallback.SdkInitResult.ERROR ->
            Log.e("MyApp", "The impossible has happened?!")
    }
}
```
**NOTE:** please ensure correct parameters (siteId, userId, zoneIds) are passed and set `isDebug` to **false** in your production/release builds.
Failure to do so may result in missing revenue or SDK initialization failure.

### Serving Flyers
#### Embedding the FlyerView
- The Shopper SDK provides a custom view, named `FlyerView`, which enables you to embed a flyer into any layout or viewgroup.
- You may do so statically (via XML) or programmatically (create a new instance of the view and add it to your ViewGroup)
- The `FlyerView` itself is simply a `FrameLayout`. For example, you can embed a flyer into a view's XML layout:

```xml 
<com.flipp.shoppersdk.views.FlyerView
    android:id="@+id/embedded_flyer_1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/black"
    android:minHeight="50px" />
```

You won't need to do anything else to serve flyers. At this point, whenever the user scrolls or brings the FlyerView onto the screen, a flyer will be displayed.

### FlyerView Features and Callbacks
#### Features
A `FlyerView` provides certain features that can be enabled/disabled depending on your use cases.
To do so, call `flyerView.setSupportedFeatures(List of Features...)`. Currently we support `ADD_TO_SHOPPING_LIST` which sends a callback (see callbacks below) when a flyer item is clicked.

#### Callbacks
You can attach a `FlyerEventsListener` to `FlyerView` instance to receive updates:

```kotlin
flyerView.setFlyerEventsListener(object: FlyerEventsListener() {
    /**
     * This signals the successful loading of a flyer.
     * Flyer content will be visible at this state
     */
    override fun onFinishedLoading() {

    }

    /**
     * This method is called when the flyer wants to be resized.
     * You don't have to do manual resizing as the webview is automatically resized.
     */
    override fun onRequestResize(heightPx: Int) {

    }

    /**
     * If IntegrationFeatures.ADD_TO_SHOPPING_LIST is passed to setSupportedFeatures(),
     * then an "Add to List" button will be shown when a flyer item is clicked.
     * Clicking that button will trigger this callback which you can use to add the item to a list.
     */
    override fun onAddItemToList(itemName: String) {

    }

    /**
     * Callback used for communication of all FlyerView level errors.
     */
    override fun onError(e: Exception) {

    }
})
```

Please note that the callbacks are optional and you do not have to listen to them. You can also override only the needed methods.

