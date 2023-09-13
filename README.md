# Flipp Shopper SDK Integration Sample
## About this Demo
This is a demo app that demonstrates the integration of Flipp Shopper SDK into a native Android app.
To try it out:
- download or checkout the code
- open in Android Studio
- sync gradle dependencies
- run the app.

## Table of Contents
- [About the SDK](#about-the-sdk)
- [Getting started](#getting-started)
  - [Adding the SDK](#adding-the-sdk)
- [Using the SDK](#using-the-sdk)
  - [Initialization](#initialization)
  - [Serving flyers](#serving-flyers)
  - [Features](#features)
  - [Callbacks](#callbacks)

# About the Flipp Shopper SDK <a name="about-the-sdk"></a>
The Flipp Shopper SDK is a framework that provides the ability to embed the Flipp NativeX experience within your Android app.

The SDK does this by providing a custom view called `FlyerView`, which has 2 main functionalities:

1. Renders the NativeX experience 
2. Provides callback methods for your app to handle events from the `FlyerView`

<img src="./assets/SDKIntegration.png" width="25%">

# Integrating Flipp Shopper SDK <a name="getting-started"></a>

### Requirements
To successfully use the SDK, you need to request the following from Flipp:
1) Access to the SDK library (Can be downloaded from this repo)
2) **publisherID**, **userID**, **siteID**, **zoneID**(s) - values required to initialize the SDK in runtime and start showing flyers

The rest of this guide assumes you have obtained the aforementioned keys.


## Adding Flipp SDK to your Project <a name="adding-the-sdk"></a>
### Step 1: Add Library to Project

#### Adding Library via JFRog (Maven Repo)
We publish the library to a private Maven repository hosted on JFrog. To access it,
please ask our team to grant access to the repository for your JFrog username.
The JFrog repository is called `shopper-sdk-droid`.

To add the repository to the project (Assuming you have the proper authorization), 
first add the repository URL to dependency resolution sources in `settings.gradle`:
```groovy
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven {
            url "https://flipplib.jfrog.io/artifactory/shopper-sdk-droid/"
            credentials {
                username "${artifactory_user}"
                password "${artifactory_password}"
            }
        }
    }
}
```
_Note: Here we are retrieving our JFrog credentials from `~/.gradle/gradle.properties` (`C:\Users\user\.gradle\gradle.properties` on Windows)
which we declared as `artifactory_user` and `artifactory_password`. You may use a similar approach or hardcode the username/password (not recommended)._

### Step 2: Declare Dependency on Library
Once library is accessible via Maven repo, 
add `implementation(group: 'com.flipp', name: 'shopper-sdk', version: 'X.Y', ext: 'aar')` to your app's dependencies,
replacing the version `'X.Y'` with actual version code (such as `1.3`).

Note: Updates to the SDK will be released over time. Flipp should notify your team if there is a critical version number to upgrade to.  

### Step 3: Sync Maven Files
Finally, make sure to hit Sync Project with Maven Files.

That's it! Now you have the Flipp Shopper SDK added and ready to use.

## Using the SDK <a name="using-the-sdk"></a>
### Initializing the SDK <a name="initialization"></a>
You will need to initialize the SDK at runtime by calling `Flipp.init` with the parameters below. 

It is preferable to call the `Flipp.init` as soon as possible, preferably from a subclass of `Application`
or from your `MainActivity`.


- ``app`` - The current application object
- ``publisherName`` - A name that uniquely identifies the client app. Use values provided by Flipp 
- ``siteId`` - An ID that represents the SDK client. Use values provided by Flipp
- ``userId`` - A unique string anonymously identifying the current user that is used for frequency cap and improving content relevancy. For example, the userId can be a hashed ADID/IDFA, hashed email, internal app or account ID. Do not send PII in plain text or hardcode a static userID
- ``isDebug`` - A boolean property indicating if this is a development environment. Use `true` for testing and `false` for production
- ``isReactNative`` - A boolean property indicating if the SDK is used in an React Native app
- ``zoneIds`` - (optional) An array of zones used to separate site/properties within a media partner's portfolio to facilitate content targeting. For example, a zone for sports, another for news or food. Use values provided by Flipp if applicable
- ``contentCode`` - (optional) A string that can be used to invoke different campaign content for testing purposes
- ``callback`` - (optional) Your callback function for handling events from the SDK


Make sure your `Application` subclass is added to the `AndroidManifest.xml`.

Here is a sample of what `Flipp.init` looks like:


```kotlin
Flipp.init(app, publisherName, siteID, userID, isDebug, isReactNative, listOf(zoneIDs), contentCode, null) {
    when (it) {
        Flipp.SdkInitCallback.SdkInitResult.OK ->
            Log.d("MyApp", "SDK initialized")
        Flipp.SdkInitCallback.SdkInitResult.ERROR ->
            Log.e("MyApp", "The impossible has happened?!")
    }
}
```

**NOTE:** Please ensure the init parameters (publisherName, siteId, userId, zoneIds) are passed and `isDebug` is set to **false** in your production/release builds.
Failure to do so may result in missing revenue or SDK initialization failure.

### Serving Flyers <a name="serving-flyers"></a>
#### Embedding the FlyerView
The Shopper SDK provides a custom view, named `FlyerView`, which enables you to embed a flyer into any layout or viewgroup.
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

**NOTE:** Do not initialize the FlyerView with 0 width/height - the SDK will not be able to detect if the experience is currently in view and thus will not trigger loading of the flyer.
## FlyerView Features and Callbacks
### Sizing Modes
Since `1.4` you can set the sizing mode of the `FlyerView` via `flyerViewInstance.autoResize` boolean (Default is `true`, _highly recommended!_).

When auto resize is set to `true`, the `FlyerView` will automatically resize to fit the flyer content perfectly.

If set to `false`, you'll have to set the size manually, both inside `onFinishedLoading(height: Int)` and `onRequestResize(height: Int)` callbacks.


### Features <a name="features"></a>
`FlyerView` provides certain features that can be enabled/disabled depending on your use case.

Currently we support the following features:
- `GO_TO_URL` - if a user clicks on a link, it is opened in the native browser. This feature must be enabled.
- `ADD_TO_SHOPPING_LIST` - (enabled by default) sends a callback when a flyer item is clicked. An example use case would be if your app has a shopping list functionality - enabling this feature would allow the SDK to send clicked items from the flyers for your app to store in the list. (See callbacks below)

To specify which features to enable, call the `setSupportedFeatures` function with a list of features:
```kotlin
flyerView.setSupportedFeatures(
    IntegrationFeatures.ADD_TO_SHOPPING_LIST,
    IntegrationFeatures.GO_TO_URL
)
```

### Callbacks <a name="callbacks"></a>
You can attach a `FlyerEventsListener` to a `FlyerView` instance to receive updates:

```kotlin
flyerView.setFlyerEventsListener(object: FlyerEventsListener() {
    /**
     * This signals the successful loading of a flyer.
     * Flyer content will be visible at this state
     */
    override fun onFinishedLoading(height: Int) {

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
