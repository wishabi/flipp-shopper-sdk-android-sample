# Flipp Shopper SDK Demo
## About this Demo
(TODO: Add info on how to get the demo running once JFrog is setup)


---
## Requirements
To successfully use the SDK, you need to request the following from us:
1) **JFrog** access credentials -- to download our SDK library
2) **UserID**, **siteID**, **zoneID**(s) -- to initialize the SDK in runtime and start showing flyers

The rest of this guide assumes you have obtained the aforementioned keys.

## Adding dependency via Gradle

[//]: # (TODO add repository info once jfrog is set up)
```
repositories {
    maven {
        url "dl.bintray.org/..."
    }

}
```

2) Add Flipp Shopper SDK to your **app module's** `build.gradle`:
   ```implementation "com.flipp.shoppersdk:1.0.+"```


## Using the SDK

### Embedding the FlyerView
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

### Initializing the SDK
You need to initialize the SDK at runtime by calling `Flipp.init` as soon as possible, preferably from a subclass of `Application`
or from your `MainActivity`:

```kotlin
Flipp.init(application, siteID, userID, true, listOf(zoneIDs)) {
    when (it) {
        Flipp.SdkInitCallback.SdkInitResult.OK ->
            Log.d("MyApp", "SDK initialized")
        Flipp.SdkInitCallback.SdkInitResult.ERROR ->
            Log.e("MyApp", "The impossible has happened?!")
    }
}
```

### Serving Flyers
You won't need to do anything else to serve flyers. At this point, whenever the user scrolls or brings the FlyerView onto the screen, a flyer will be displayed.

### FlyerView Callbacks
You may like to integrate with the SDK via the callbacks it provides.Currently, it gives a few callbacks:

- `addItemToList(item: String)`: This gives you the ability to create a shopping list or similar app.
- `requestResize(height: Int)`: This is called when the flyer is resized.
- 