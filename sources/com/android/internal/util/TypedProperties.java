package com.android.internal.util;

import android.app.slice.SliceItem;
import com.miui.enterprise.sdk.ApplicationManager;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TypedProperties extends HashMap<String, Object> {
    static final String NULL_STRING = new String("<TypedProperties:NULL_STRING>");
    public static final int STRING_NOT_SET = -1;
    public static final int STRING_NULL = 0;
    public static final int STRING_SET = 1;
    public static final int STRING_TYPE_MISMATCH = -2;
    static final int TYPE_BOOLEAN = 90;
    static final int TYPE_BYTE = 329;
    static final int TYPE_DOUBLE = 2118;
    static final int TYPE_ERROR = -1;
    static final int TYPE_FLOAT = 1094;
    static final int TYPE_INT = 1097;
    static final int TYPE_LONG = 2121;
    static final int TYPE_SHORT = 585;
    static final int TYPE_STRING = 29516;
    static final int TYPE_UNSET = 120;

    public static class ParseException extends IllegalArgumentException {
        ParseException(StreamTokenizer state, String expected) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected ");
            stringBuilder.append(expected);
            stringBuilder.append(", saw ");
            stringBuilder.append(state.toString());
            super(stringBuilder.toString());
        }
    }

    public static class TypeException extends IllegalArgumentException {
        TypeException(String property, Object value, String requestedType) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(property);
            stringBuilder.append(" has type ");
            stringBuilder.append(value.getClass().getName());
            stringBuilder.append(", not ");
            stringBuilder.append(requestedType);
            super(stringBuilder.toString());
        }
    }

    static StreamTokenizer initTokenizer(Reader r) {
        StreamTokenizer st = new StreamTokenizer(r);
        st.resetSyntax();
        st.wordChars(48, 57);
        st.wordChars(65, 90);
        st.wordChars(97, 122);
        st.wordChars(95, 95);
        st.wordChars(36, 36);
        st.wordChars(46, 46);
        st.wordChars(45, 45);
        st.wordChars(43, 43);
        st.ordinaryChar(61);
        st.whitespaceChars(32, 32);
        st.whitespaceChars(9, 9);
        st.whitespaceChars(10, 10);
        st.whitespaceChars(13, 13);
        st.quoteChar(34);
        st.slashStarComments(true);
        st.slashSlashComments(true);
        return st;
    }

    static int interpretType(String typeName) {
        if ("unset".equals(typeName)) {
            return 120;
        }
        if ("boolean".equals(typeName)) {
            return 90;
        }
        if ("byte".equals(typeName)) {
            return 329;
        }
        if ("short".equals(typeName)) {
            return 585;
        }
        if (SliceItem.FORMAT_INT.equals(typeName)) {
            return 1097;
        }
        if ("long".equals(typeName)) {
            return 2121;
        }
        if (ApplicationManager.FLOAT.equals(typeName)) {
            return 1094;
        }
        if ("double".equals(typeName)) {
            return 2118;
        }
        if ("String".equals(typeName)) {
            return TYPE_STRING;
        }
        return -1;
    }

    static void parse(Reader r, Map<String, Object> map) throws ParseException, IOException {
        StreamTokenizer st = initTokenizer(r);
        String identifierPattern = "[a-zA-Z_$][0-9a-zA-Z_$]*";
        Pattern propertyNamePattern = Pattern.compile("([a-zA-Z_$][0-9a-zA-Z_$]*\\.)*[a-zA-Z_$][0-9a-zA-Z_$]*");
        while (true) {
            int token = st.nextToken();
            if (token != -1) {
                if (token == -3) {
                    int type = interpretType(st.sval);
                    if (type != -1) {
                        st.sval = null;
                        if (type == 120 && st.nextToken() != 40) {
                            throw new ParseException(st, "'('");
                        } else if (st.nextToken() == -3) {
                            String propertyName = st.sval;
                            if (propertyNamePattern.matcher(propertyName).matches()) {
                                st.sval = null;
                                if (type == 120) {
                                    if (st.nextToken() == 41) {
                                        map.remove(propertyName);
                                    } else {
                                        throw new ParseException(st, "')'");
                                    }
                                } else if (st.nextToken() == 61) {
                                    Object value = parseValue(st, type);
                                    Object oldValue = map.remove(propertyName);
                                    if (oldValue == null || value.getClass() == oldValue.getClass()) {
                                        map.put(propertyName, value);
                                    } else {
                                        throw new ParseException(st, "(property previously declared as a different type)");
                                    }
                                } else {
                                    throw new ParseException(st, "'='");
                                }
                                if (st.nextToken() != 59) {
                                    throw new ParseException(st, "';'");
                                }
                            } else {
                                throw new ParseException(st, "valid property name");
                            }
                        } else {
                            throw new ParseException(st, "property name");
                        }
                    }
                    throw new ParseException(st, "valid type name");
                }
                throw new ParseException(st, "type name");
            }
            return;
        }
    }

    static Object parseValue(StreamTokenizer st, int type) throws IOException {
        int token = st.nextToken();
        String str;
        if (type == 90) {
            str = "boolean constant";
            if (token == -3) {
                if ("true".equals(st.sval)) {
                    return Boolean.TRUE;
                }
                if ("false".equals(st.sval)) {
                    return Boolean.FALSE;
                }
                throw new ParseException(st, str);
            }
            throw new ParseException(st, str);
        } else if ((type & 255) == 73) {
            str = "integer constant";
            if (token == -3) {
                try {
                    long value = Long.decode(st.sval).longValue();
                    int width = (type >> 8) & 255;
                    if (width != 1) {
                        if (width != 2) {
                            if (width != 4) {
                                if (width != 8) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Internal error; unexpected integer type width ");
                                    stringBuilder.append(width);
                                    throw new IllegalStateException(stringBuilder.toString());
                                } else if (value >= Long.MIN_VALUE && value <= Long.MAX_VALUE) {
                                    return new Long(value);
                                } else {
                                    throw new ParseException(st, "64-bit integer constant");
                                }
                            } else if (value >= -2147483648L && value <= 2147483647L) {
                                return new Integer((int) value);
                            } else {
                                throw new ParseException(st, "32-bit integer constant");
                            }
                        } else if (value >= -32768 && value <= 32767) {
                            return new Short((short) ((int) value));
                        } else {
                            throw new ParseException(st, "16-bit integer constant");
                        }
                    } else if (value >= -128 && value <= 127) {
                        return new Byte((byte) ((int) value));
                    } else {
                        throw new ParseException(st, "8-bit integer constant");
                    }
                } catch (NumberFormatException e) {
                    throw new ParseException(st, str);
                }
            }
            throw new ParseException(st, str);
        } else if ((type & 255) == 70) {
            str = "float constant";
            if (token == -3) {
                try {
                    double value2 = Double.parseDouble(st.sval);
                    if (((type >> 8) & 255) != 4) {
                        return new Double(value2);
                    }
                    double absValue = Math.abs(value2);
                    if (absValue == 0.0d || Double.isInfinite(value2) || Double.isNaN(value2) || (absValue >= 1.401298464324817E-45d && absValue <= 3.4028234663852886E38d)) {
                        return new Float((float) value2);
                    }
                    throw new ParseException(st, "32-bit float constant");
                } catch (NumberFormatException e2) {
                    throw new ParseException(st, str);
                }
            }
            throw new ParseException(st, str);
        } else if (type != TYPE_STRING) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Internal error; unknown type ");
            stringBuilder2.append(type);
            throw new IllegalStateException(stringBuilder2.toString());
        } else if (token == 34) {
            return st.sval;
        } else {
            if (token == -3) {
                if ("null".equals(st.sval)) {
                    return NULL_STRING;
                }
            }
            throw new ParseException(st, "double-quoted string or 'null'");
        }
    }

    public void load(Reader r) throws IOException {
        parse(r, this);
    }

    public Object get(Object key) {
        String value = super.get(key);
        if (value == NULL_STRING) {
            return null;
        }
        return value;
    }

    public boolean getBoolean(String property, boolean def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        throw new TypeException(property, value, "boolean");
    }

    public byte getByte(String property, byte def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Byte) {
            return ((Byte) value).byteValue();
        }
        throw new TypeException(property, value, "byte");
    }

    public short getShort(String property, short def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        throw new TypeException(property, value, "short");
    }

    public int getInt(String property, int def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        throw new TypeException(property, value, SliceItem.FORMAT_INT);
    }

    public long getLong(String property, long def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Long) {
            return ((Long) value).longValue();
        }
        throw new TypeException(property, value, "long");
    }

    public float getFloat(String property, float def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Float) {
            return ((Float) value).floatValue();
        }
        throw new TypeException(property, value, ApplicationManager.FLOAT);
    }

    public double getDouble(String property, double def) {
        Object value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value instanceof Double) {
            return ((Double) value).doubleValue();
        }
        throw new TypeException(property, value, "double");
    }

    public String getString(String property, String def) {
        String value = super.get(property);
        if (value == null) {
            return def;
        }
        if (value == NULL_STRING) {
            return null;
        }
        if (value instanceof String) {
            return value;
        }
        throw new TypeException(property, value, "string");
    }

    public boolean getBoolean(String property) {
        return getBoolean(property, false);
    }

    public byte getByte(String property) {
        return getByte(property, (byte) 0);
    }

    public short getShort(String property) {
        return getShort(property, (short) 0);
    }

    public int getInt(String property) {
        return getInt(property, 0);
    }

    public long getLong(String property) {
        return getLong(property, 0);
    }

    public float getFloat(String property) {
        return getFloat(property, 0.0f);
    }

    public double getDouble(String property) {
        return getDouble(property, 0.0d);
    }

    public String getString(String property) {
        return getString(property, "");
    }

    public int getStringInfo(String property) {
        String value = super.get(property);
        if (value == null) {
            return -1;
        }
        if (value == NULL_STRING) {
            return 0;
        }
        if (value instanceof String) {
            return 1;
        }
        return -2;
    }
}
