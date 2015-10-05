# AndroidTagLayout
A custom view that nicely displays a list of selectable tags.

[ ![Download](https://api.bintray.com/packages/flavp/maven/taglayout/images/download.svg) ](https://bintray.com/flavp/maven/taglayout/_latestVersion)

![TagLayout Screenshot](screenshot1.png)

## Usage

In your layout resource:

    <com.flavpopescu.TagLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        ...
        custom:com.flavpopescu.tags="@array/tags_sample"
        custom:com.flavpopescu.tagBackground="@drawable/rounded_rectangle"
        custom:com.flavpopescu.tagBackgroundSelected="@drawable/rounded_rectangle_selected"
        custom:com.flavpopescu.tagTextColor="@android:color/white"
        custom:com.flavpopescu.tagTextFont="fonts/Roboto-Medium.ttf"
        custom:com.flavpopescu.tagVerticalSpacing="8dp"
        custom:com.flavpopescu.tagTextPadding="@dimen/tag_text_padding"
    />

The attribute `tagTextPadding` is mandatory if you are using custom background drawables which add some padding around the text (see the sample app) and must be set to the same value. Otherwise, the expected tag widths would be incorrectly calculated resulting in clipped views.

## Installation

Add the library as a dependency in your module's `build.gradle`:

    dependencies {
      ...
      compile 'com.flavpopescu:taglayout:+'
    }
    
Make sure you have `jcenter()` added as a maven repo in your project's `build.gradle` (newer Android Studio version already include this by default):

    repositories {
        jcenter()
    }
