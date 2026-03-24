package TOPSECRET.domain.ValueObjects;

public class Picture {

        private final String _picture; // path or URL

        public Picture(String picture) {
            if (picture == null || picture.isBlank()) {
                throw new IllegalArgumentException("Picture cannot be null or blank");
            }
            this._picture = picture;
        }

        public String get_picture() {
            return _picture;
        }
}
