# Umbrella
Improvements made
- Services aligned into modularized interfaces
- ZoneService pre loads json into database for faster, easier querying
- App automatically finds closest zip code
- Formal dependency injection with Dagger 2
- 90% Kotlin :hands:
- Better(?) naming conventions

What would I have done if given more time?
- Make Home use a "real" action bar (using menus and titles instead of manually inserted layouts)
- Offline Persistence
- More animations/transitions
- Better onboarding process
- Figure out why original picasso settings weren't working
- Image API doesn't provide images for all scenarios? Would look into this
- Use actual settings preferences provided by Android 
- Fix up alert dialogs used in settings
- Better handling of error scenarios

Outstanding bugs
- Last instance of high/low temps are highlights, as opposed to the _first_ instance.
