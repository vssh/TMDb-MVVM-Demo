## Approach
It is a relatively simple app with limited interactions, so I just used a single `Activity`.
It has 2 `Fragments`, one for showing the search results and another showing the details.
Until now, I have generally used the MVP architecture, but since this was a smaller project from start, I decide to use MVVM architecture.
For this I used parts of the Android Architecture Components, `Live Data` and `ViewModel` along with `Data Binding`.
The `Activity` instantiates the `ViewModel`, and this is also available to both the `Fragments`.
This makes coordination between `Fragments` much easier as they share the `ViewModel`, which stores all the state information.

## Libraries used
####  Retrofit with Gson-converter
Retrofit is used to communicate with the TMDb API. It allows easy request generation and response decoding.

#### Glide
Glide is used to handle poster and backdrop images. It fetches them from the URL and loads them into the `ImageView`.

## Challenges
The main challenge was to get used to the MVVM architecture, but also reduced the boilerplate code and was much faster development methodology overall.
The other issue was that Android's default `SearchView` widget only takes a `CursorAdapter` for suggestions, but I had decided against using a database for storing just 10 strings.
So, I had to use a `MatrixCursor`, allowing me to provide a `Cursor` from `Array`.

## Additions
In addition to the requirements, I made two additions:
-  The user can choose to toggle between searching the Movies and TV series. All data still comes from TMDb.
-  The details view also uses the backdrop image from response and uses that as the background image. I find the effect much more pleasant than otherwise.
