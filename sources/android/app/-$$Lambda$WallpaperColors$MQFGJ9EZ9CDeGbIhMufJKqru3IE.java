package android.app;

import com.android.internal.graphics.palette.Palette.Swatch;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WallpaperColors$MQFGJ9EZ9CDeGbIhMufJKqru3IE implements Comparator {
    public static final /* synthetic */ -$$Lambda$WallpaperColors$MQFGJ9EZ9CDeGbIhMufJKqru3IE INSTANCE = new -$$Lambda$WallpaperColors$MQFGJ9EZ9CDeGbIhMufJKqru3IE();

    private /* synthetic */ -$$Lambda$WallpaperColors$MQFGJ9EZ9CDeGbIhMufJKqru3IE() {
    }

    public final int compare(Object obj, Object obj2) {
        return (((Swatch) obj2).getPopulation() - ((Swatch) obj).getPopulation());
    }
}
