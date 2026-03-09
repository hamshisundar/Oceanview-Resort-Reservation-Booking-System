# Images

Place hero and room images here for production use.

**Home page hero:** Save your ocean/sunset image as **`hero.png`** (or `hero.jpg`) in this folder. The home page will display it in the hero section. If the file is missing, the blue gradient background still shows.

**Room images:** Use a `rooms/` subfolder, e.g.:
- `rooms/standard.jpg`
- `rooms/deluxe.jpg`
- `rooms/oceanview.jpg`
- `rooms/suite.jpg`

Update room `image_path` in the database and reference in JSP as needed. The UI uses placeholder styling when image paths are missing.
