package android.filterpacks.imageproc;

import android.app.admin.DevicePolicyManager;
import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.os.BatteryManager;
import android.provider.Downloads.Impl;
import android.util.DisplayMetrics;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import org.ifaa.android.manager.face.IFAAFaceManager;

public class AutoFixFilter extends Filter {
    private static final int[] normal_cdf = new int[]{9, 33, 50, 64, 75, 84, 92, 99, 106, 112, 117, 122, 126, 130, 134, 138, 142, 145, 148, 150, 154, 157, 159, 162, 164, 166, 169, 170, 173, 175, 177, 179, 180, 182, 184, 186, 188, 189, 190, 192, 194, 195, 197, 198, 199, 200, 202, 203, 205, 206, 207, 208, 209, 210, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 229, 230, 231, 232, 233, 234, 235, 236, 236, 237, 238, 239, 239, 240, 240, 242, 242, 243, 244, 245, 245, 246, 247, 247, 248, 249, 249, 250, 250, 251, 252, 253, 253, 254, 255, 255, 256, 256, 257, 258, 258, 259, 259, 259, 260, 261, 262, 262, 263, 263, 264, 264, 265, 265, 266, 267, 267, 268, 268, 269, 269, 269, 270, 270, 271, 272, 272, 273, 273, 274, 274, 275, 275, 276, 276, 277, 277, 277, 278, 278, 279, 279, 279, 280, 280, 281, 282, 282, 282, 283, 283, 284, 284, 285, 285, 285, 286, 286, 287, 287, 288, 288, 288, 289, 289, 289, 290, 290, 290, 291, 292, 292, 292, 293, 293, 294, 294, 294, 295, 295, 296, 296, 296, 297, 297, 297, 298, 298, 298, 299, 299, 299, 299, 300, 300, 301, 301, 302, 302, 302, 303, 303, 304, 304, 304, 305, 305, 305, 306, 306, 306, 307, 307, 307, 308, 308, 308, 309, 309, 309, 309, 310, 310, 310, 310, 311, 312, 312, 312, 313, 313, 313, 314, 314, 314, 315, 315, 315, 315, 316, 316, 316, 317, 317, 317, 318, 318, 318, 319, 319, 319, 319, 319, 320, 320, 320, 321, 321, 322, 322, 322, 323, 323, 323, 323, 324, 324, 324, 325, 325, 325, 325, 326, 326, 326, 327, 327, 327, 327, 328, 328, 328, 329, 329, 329, 329, 329, 330, 330, 330, 330, 331, 331, 332, 332, 332, 333, 333, 333, 333, 334, 334, 334, 334, 335, 335, 335, 336, 336, 336, 336, 337, 337, 337, 337, 338, 338, 338, 339, 339, 339, 339, 339, 339, 340, 340, 340, 340, 341, 341, 342, 342, 342, 342, 343, 343, 343, 344, 344, 344, 344, 345, 345, 345, 345, 346, 346, 346, 346, 347, 347, 347, 347, 348, 348, 348, 348, 349, 349, 349, 349, 349, 349, 350, 350, 350, 350, 351, 351, 352, 352, 352, 352, 353, 353, 353, 353, 354, 354, 354, 354, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_FOLDER, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_FOLDER, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_FOLDER, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_FOLDER, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_PACKAGE, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_PACKAGE, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_PACKAGE, MetricsEvent.ACTION_SCOPED_DIRECTORY_ACCESS_DENIED_AND_PERSIST_BY_PACKAGE, MetricsEvent.OVERVIEW_DISMISS_ALL, MetricsEvent.OVERVIEW_DISMISS_ALL, MetricsEvent.OVERVIEW_DISMISS_ALL, MetricsEvent.OVERVIEW_DISMISS_ALL, MetricsEvent.QS_EDIT, MetricsEvent.QS_EDIT, MetricsEvent.QS_EDIT, MetricsEvent.QS_EDIT, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, MetricsEvent.ACTION_QS_EDIT_RESET, 360, 360, 360, 360, 361, 361, 362, 362, 362, 362, 363, 363, 363, 363, 364, 364, 364, 364, 365, 365, 365, 365, 366, 366, 366, 366, 366, 367, 367, 367, 367, 368, 368, 368, 368, 369, 369, 369, 369, 369, 369, 370, 370, 370, 370, 370, 371, 371, 372, 372, 372, 372, 373, 373, 373, 373, 374, 374, 374, 374, 374, 375, 375, 375, 375, 376, 376, 376, 376, 377, 377, 377, 377, 378, 378, 378, 378, 378, 379, 379, 379, 379, 379, 379, 380, 380, 380, 380, 381, 381, 381, 382, 382, 382, 382, 383, 383, 383, 383, 384, 384, 384, 384, 385, 385, 385, 385, 385, 386, 386, 386, 386, 387, 387, 387, 387, 388, 388, 388, 388, 388, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_DOCK_RESIZE, MetricsEvent.ACTION_WINDOW_UNDOCK_MAX, MetricsEvent.ACTION_WINDOW_UNDOCK_MAX, MetricsEvent.ACTION_WINDOW_UNDOCK_MAX, MetricsEvent.ACTION_WINDOW_UNDOCK_MAX, MetricsEvent.ACTION_WINDOW_DOCK_UNRESIZABLE, MetricsEvent.ACTION_WINDOW_DOCK_UNRESIZABLE, MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.ACTION_TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.ACTION_TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.ACTION_TUNER_POWER_NOTIFICATION_CONTROLS, MetricsEvent.ACTION_TUNER_POWER_NOTIFICATION_CONTROLS, 394, 394, 394, 394, 395, 395, 395, 395, 396, 396, 396, 396, 396, MetricsEvent.ACTION_REMOTE_INPUT_OPEN, MetricsEvent.ACTION_REMOTE_INPUT_OPEN, MetricsEvent.ACTION_REMOTE_INPUT_OPEN, MetricsEvent.ACTION_REMOTE_INPUT_OPEN, 398, 398, 398, 398, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, MetricsEvent.ACTION_REMOTE_INPUT_FAIL, 400, 400, 400, 400, 400, 401, 401, 402, 402, 402, 402, 403, 403, 403, 403, 404, 404, 404, 404, 405, 405, 405, 405, 406, 406, 406, 406, 406, 407, 407, 407, 407, 408, 408, 408, 408, 409, 409, 409, 409, 409, 409, 410, 410, 410, 410, 411, 411, 412, 412, 412, 412, IFAAFaceManager.STATUS_FACE_OFFSET_BOTTOM, IFAAFaceManager.STATUS_FACE_OFFSET_BOTTOM, IFAAFaceManager.STATUS_FACE_OFFSET_BOTTOM, IFAAFaceManager.STATUS_FACE_OFFSET_BOTTOM, IFAAFaceManager.STATUS_FACE_CAPTURE, IFAAFaceManager.STATUS_FACE_CAPTURE, IFAAFaceManager.STATUS_FACE_CAPTURE, IFAAFaceManager.STATUS_FACE_CAPTURE, IFAAFaceManager.STATUS_FACE_NO_FACE, IFAAFaceManager.STATUS_FACE_NO_FACE, IFAAFaceManager.STATUS_FACE_NO_FACE, IFAAFaceManager.STATUS_FACE_NO_FACE, 416, 416, 416, 416, IFAAFaceManager.STATUS_FACE_TOO_FAST, IFAAFaceManager.STATUS_FACE_TOO_FAST, IFAAFaceManager.STATUS_FACE_TOO_FAST, IFAAFaceManager.STATUS_FACE_TOO_FAST, IFAAFaceManager.STATUS_FACE_HACKER, IFAAFaceManager.STATUS_FACE_HACKER, IFAAFaceManager.STATUS_FACE_HACKER, IFAAFaceManager.STATUS_FACE_HACKER, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, IFAAFaceManager.STATUS_FACE_IMAGE_DIRTY, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_420, 421, 421, 422, 422, 422, 422, 423, 423, 423, 423, 424, 424, 424, 425, 425, 425, 425, 426, 426, 426, 426, 427, 427, 427, 427, 428, 428, 428, 429, 429, 429, 429, 429, 429, 430, 430, 430, 430, 431, 431, DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER, DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER, DevicePolicyManager.PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER, 433, 433, 433, 433, 434, 434, 434, 435, 435, 435, 435, 436, 436, 436, 436, 437, 437, 437, 438, 438, 438, 438, 439, 439, 439, 439, 439, 440, 440, 440, 441, 441, 442, 442, 442, 443, 443, 443, 443, 444, 444, 444, 445, 445, 445, 446, 446, 446, 446, 447, 447, 447, 448, 448, 448, 449, 449, 449, 449, 449, DisplayMetrics.DENSITY_450, DisplayMetrics.DENSITY_450, DisplayMetrics.DENSITY_450, MetricsEvent.ACTION_SHOW_APP_DISAMBIG_APP_FEATURED, MetricsEvent.ACTION_SHOW_APP_DISAMBIG_APP_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED, MetricsEvent.ACTION_SHOW_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_SHOW_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_SHOW_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED, MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS, MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS, MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS, MetricsEvent.ACTION_APP_DISAMBIG_JUST_ONCE, MetricsEvent.ACTION_APP_DISAMBIG_JUST_ONCE, MetricsEvent.ACTION_APP_DISAMBIG_JUST_ONCE, MetricsEvent.ACTION_APP_DISAMBIG_TAP, MetricsEvent.ACTION_APP_DISAMBIG_TAP, MetricsEvent.ACTION_APP_DISAMBIG_TAP, 458, 458, 458, 459, 459, 459, 459, MetricsEvent.ACTION_DELETION_SELECTION_PHOTOS, MetricsEvent.ACTION_DELETION_SELECTION_PHOTOS, MetricsEvent.ACTION_DELETION_SELECTION_PHOTOS, MetricsEvent.ACTION_DELETION_SELECTION_ALL_APPS, MetricsEvent.ACTION_DELETION_SELECTION_ALL_APPS, MetricsEvent.ACTION_DELETION_SELECTION_APP_ON, MetricsEvent.ACTION_DELETION_SELECTION_APP_ON, MetricsEvent.ACTION_DELETION_SELECTION_APP_ON, MetricsEvent.ACTION_DELETION_SELECTION_APP_OFF, MetricsEvent.ACTION_DELETION_SELECTION_APP_OFF, MetricsEvent.ACTION_DELETION_SELECTION_APP_OFF, MetricsEvent.ACTION_DELETION_APPS_COLLAPSED, MetricsEvent.ACTION_DELETION_APPS_COLLAPSED, MetricsEvent.ACTION_DELETION_SELECTION_DOWNLOADS, MetricsEvent.ACTION_DELETION_SELECTION_DOWNLOADS, MetricsEvent.ACTION_DELETION_SELECTION_DOWNLOADS, MetricsEvent.ACTION_DELETION_DOWNLOADS_COLLAPSED, MetricsEvent.ACTION_DELETION_DOWNLOADS_COLLAPSED, MetricsEvent.ACTION_DELETION_DOWNLOADS_COLLAPSED, MetricsEvent.ACTION_DELETION_HELPER_CLEAR, MetricsEvent.ACTION_DELETION_HELPER_CLEAR, MetricsEvent.ACTION_DELETION_HELPER_CLEAR, MetricsEvent.ACTION_DELETION_HELPER_CANCEL, MetricsEvent.ACTION_DELETION_HELPER_CANCEL, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CONFIRM, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CONFIRM, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CONFIRM, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CONFIRM, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, MetricsEvent.ACTION_DELETION_HELPER_APPS_DELETION_FAIL, MetricsEvent.ACTION_DELETION_HELPER_DOWNLOADS_DELETION_FAIL, MetricsEvent.ACTION_DELETION_HELPER_DOWNLOADS_DELETION_FAIL, MetricsEvent.ACTION_DELETION_HELPER_DOWNLOADS_DELETION_FAIL, MetricsEvent.ACTION_DELETION_HELPER_PHOTOS_VIDEOS_DELETION_FAIL, MetricsEvent.ACTION_DELETION_HELPER_PHOTOS_VIDEOS_DELETION_FAIL, MetricsEvent.DASHBOARD_CONTAINER, MetricsEvent.DASHBOARD_CONTAINER, MetricsEvent.DASHBOARD_CONTAINER, MetricsEvent.SUPPORT_FRAGMENT, MetricsEvent.SUPPORT_FRAGMENT, MetricsEvent.ACTION_SELECT_SUMMARY, MetricsEvent.ACTION_SELECT_SUMMARY, MetricsEvent.ACTION_SELECT_SUMMARY, MetricsEvent.ACTION_SELECT_SUPPORT_FRAGMENT, MetricsEvent.ACTION_SELECT_SUPPORT_FRAGMENT, MetricsEvent.ACTION_SUPPORT_TIPS_AND_TRICKS, MetricsEvent.ACTION_SUPPORT_TIPS_AND_TRICKS, MetricsEvent.ACTION_SUPPORT_TIPS_AND_TRICKS, MetricsEvent.ACTION_SUPPORT_HELP_AND_FEEDBACK, MetricsEvent.ACTION_SUPPORT_HELP_AND_FEEDBACK, MetricsEvent.ACTION_SUPPORT_HELP_AND_FEEDBACK, 480, 480, 480, MetricsEvent.ACTION_SUPPORT_PHONE, MetricsEvent.ACTION_SUPPORT_CHAT, MetricsEvent.ACTION_SUPPORT_CHAT, MetricsEvent.ACTION_SUPPORT_DISCLAIMER_CANCEL, MetricsEvent.ACTION_SUPPORT_DISCLAIMER_CANCEL, MetricsEvent.ACTION_SUPPORT_DISCLAIMER_OK, MetricsEvent.ACTION_SUPPORT_DISCLAIMER_OK, MetricsEvent.ACTION_SUPPORT_DISCLAIMER_OK, MetricsEvent.ACTION_SUPPORT_DAIL_TOLLFREE, MetricsEvent.ACTION_SUPPORT_DAIL_TOLLFREE, MetricsEvent.ACTION_SUPPORT_VIEW_TRAVEL_ABROAD_DIALOG, MetricsEvent.ACTION_SUPPORT_VIEW_TRAVEL_ABROAD_DIALOG, MetricsEvent.ACTION_SUPPORT_DIAL_TOLLED, MetricsEvent.ACTION_SUPPORT_DIAL_TOLLED, 488, 488, 488, 489, 489, 489, 490, 490, 491, 492, 492, 493, 493, 494, 494, 495, 495, 496, 496, Impl.STATUS_TOO_MANY_REDIRECTS, Impl.STATUS_TOO_MANY_REDIRECTS, 498, 498, 499, 499, 499, 500, 501, 502, 502, 503, 503, 504, 504, 505, 505, 506, 507, 507, 508, 508, 509, 509, 510, 510, 511, 512, 513, 513, 514, 515, 515, 516, 517, 517, 518, 519, 519, 519, 520, 521, 522, 523, 524, 524, 525, MetricsEvent.DIALOG_SUPPORT_DISCLAIMER, MetricsEvent.DIALOG_SUPPORT_DISCLAIMER, MetricsEvent.DIALOG_SUPPORT_PHONE, 528, 529, 529, 530, 531, 532, 533, 534, 535, 535, 536, MetricsEvent.DIALOG_NO_HOME, 538, MetricsEvent.DIALOG_BLUETOOTH_PAIRED_DEVICE_PROFILE, MetricsEvent.DIALOG_BLUETOOTH_PAIRED_DEVICE_PROFILE, 540, MetricsEvent.DIALOG_WPS_SETUP, 543, MetricsEvent.DIALOG_WIFI_SKIP, 545, 546, 547, 548, 549, 549, 550, 552, 553, 554, 555, 556, 558, 559, 559, 561, 562, 564, 565, 566, 568, 569, 570, MetricsEvent.DIALOG_FINGERPRINT_CANCEL_SETUP, 574, 575, 577, 578, 579, MetricsEvent.DIALOG_AP_SETTINGS, 583, 585, 587, 589, 590, 593, 595, 597, 599, 602, MetricsEvent.DIALOG_WIFI_PBC, 607, 609, MetricsEvent.PROVISIONING_EXTRA, MetricsEvent.PROVISIONING_ENTRY_POINT_NFC, MetricsEvent.PROVISIONING_ENTRY_POINT_TRUSTED_SOURCE, MetricsEvent.PROVISIONING_CREATE_PROFILE_TASK_MS, MetricsEvent.PROVISIONING_CANCELLED, 628, MetricsEvent.ACTION_PERMISSION_GRANT_UNKNOWN, MetricsEvent.ACTION_PERMISSION_GRANT_READ_CALENDAR, MetricsEvent.ACTION_PERMISSION_GRANT_WRITE_CALENDAR, MetricsEvent.ACTION_PERMISSION_DENIED_CAMERA, MetricsEvent.ACTION_PERMISSION_REVOKE_READ_CONTACTS, MetricsEvent.ACTION_PERMISSION_REQUEST_GET_ACCOUNTS, MetricsEvent.ACTION_PERMISSION_GRANT_ACCESS_FINE_LOCATION, MetricsEvent.ACTION_PERMISSION_REQUEST_RECORD_AUDIO, MetricsEvent.ACTION_PERMISSION_REVOKE_READ_PHONE_STATE, MetricsEvent.ACTION_PERMISSION_DENIED_READ_CALL_LOG, MetricsEvent.ACTION_PERMISSION_REQUEST_USE_SIP, 700, MetricsEvent.ACTION_PERMISSION_REQUEST_READ_SMS};
    private final String mAutoFixShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float scale;\nuniform float shift_scale;\nuniform float hist_offset;\nuniform float hist_scale;\nuniform float density_offset;\nuniform float density_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.33333, 0.33333, 0.33333);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = dot(color.rgb, weights);\n  float mask_value = energy - 0.5;\n  float alpha;\n  if (mask_value > 0.0) {\n    alpha = (pow(2.0 * mask_value, 1.5) - 1.0) * scale + 1.0;\n  } else { \n    alpha = (pow(2.0 * mask_value, 2.0) - 1.0) * scale + 1.0;\n  }\n  float index = energy * hist_scale + hist_offset;\n  vec4 temp = texture2D(tex_sampler_1, vec2(index, 0.5));\n  float value = temp.g + temp.r * shift_scale;\n  index = value * density_scale + density_offset;\n  temp = texture2D(tex_sampler_2, vec2(index, 0.5));\n  value = temp.g + temp.r * shift_scale;\n  float dst_energy = energy * alpha + value * (1.0 - alpha);\n  float max_energy = energy / max(color.r, max(color.g, color.b));\n  if (dst_energy > max_energy) {\n    dst_energy = max_energy;\n  }\n  if (energy == 0.0) {\n    gl_FragColor = color;\n  } else {\n    gl_FragColor = vec4(color.rgb * dst_energy / energy, color.a);\n  }\n}\n";
    private Frame mDensityFrame;
    private int mHeight = 0;
    private Frame mHistFrame;
    private Program mNativeProgram;
    @GenerateFieldPort(name = "scale")
    private float mScale;
    private Program mShaderProgram;
    private int mTarget = 0;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    private int mTileSize = 640;
    private int mWidth = 0;

    public AutoFixFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        MutableFrameFormat create = ImageFormat.create(3);
        String str = SliceItem.FORMAT_IMAGE;
        addMaskedInputPort(str, create);
        addOutputBasedOnInput(str, str);
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public void initProgram(FilterContext context, int target) {
        if (target == 3) {
            ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float scale;\nuniform float shift_scale;\nuniform float hist_offset;\nuniform float hist_scale;\nuniform float density_offset;\nuniform float density_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.33333, 0.33333, 0.33333);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = dot(color.rgb, weights);\n  float mask_value = energy - 0.5;\n  float alpha;\n  if (mask_value > 0.0) {\n    alpha = (pow(2.0 * mask_value, 1.5) - 1.0) * scale + 1.0;\n  } else { \n    alpha = (pow(2.0 * mask_value, 2.0) - 1.0) * scale + 1.0;\n  }\n  float index = energy * hist_scale + hist_offset;\n  vec4 temp = texture2D(tex_sampler_1, vec2(index, 0.5));\n  float value = temp.g + temp.r * shift_scale;\n  index = value * density_scale + density_offset;\n  temp = texture2D(tex_sampler_2, vec2(index, 0.5));\n  value = temp.g + temp.r * shift_scale;\n  float dst_energy = energy * alpha + value * (1.0 - alpha);\n  float max_energy = energy / max(color.r, max(color.g, color.b));\n  if (dst_energy > max_energy) {\n    dst_energy = max_energy;\n  }\n  if (energy == 0.0) {\n    gl_FragColor = color;\n  } else {\n    gl_FragColor = vec4(color.rgb * dst_energy / energy, color.a);\n  }\n}\n");
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mShaderProgram = shaderProgram;
            this.mTarget = target;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Filter Sharpen does not support frames of target ");
        stringBuilder.append(target);
        stringBuilder.append("!");
        throw new RuntimeException(stringBuilder.toString());
    }

    private void initParameters() {
        this.mShaderProgram.setHostValue("shift_scale", Float.valueOf(0.00390625f));
        this.mShaderProgram.setHostValue("hist_offset", Float.valueOf(6.527415E-4f));
        this.mShaderProgram.setHostValue("hist_scale", Float.valueOf(0.99869454f));
        this.mShaderProgram.setHostValue("density_offset", Float.valueOf(4.8828125E-4f));
        this.mShaderProgram.setHostValue("density_scale", Float.valueOf(0.99902344f));
        this.mShaderProgram.setHostValue(BatteryManager.EXTRA_SCALE, Float.valueOf(this.mScale));
    }

    /* Access modifiers changed, original: protected */
    public void prepare(FilterContext context) {
        int[] densityTable = new int[1024];
        for (int i = 0; i < 1024; i++) {
            densityTable[i] = (int) ((((long) normal_cdf[i]) * 65535) / ((long) 766));
        }
        this.mDensityFrame = context.getFrameManager().newFrame(ImageFormat.create(1024, 1, 3, 3));
        this.mDensityFrame.setInts(densityTable);
    }

    public void tearDown(FilterContext context) {
        Frame frame = this.mDensityFrame;
        if (frame != null) {
            frame.release();
            this.mDensityFrame = null;
        }
        frame = this.mHistFrame;
        if (frame != null) {
            frame.release();
            this.mHistFrame = null;
        }
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        Program program = this.mShaderProgram;
        if (program != null) {
            program.setHostValue(BatteryManager.EXTRA_SCALE, Float.valueOf(this.mScale));
        }
    }

    public void process(FilterContext context) {
        String str = SliceItem.FORMAT_IMAGE;
        Frame input = pullInput(str);
        FrameFormat inputFormat = input.getFormat();
        if (this.mShaderProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
            initParameters();
        }
        if (!(inputFormat.getWidth() == this.mWidth && inputFormat.getHeight() == this.mHeight)) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
            createHistogramFrame(context, this.mWidth, this.mHeight, input.getInts());
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        this.mShaderProgram.process(new Frame[]{input, this.mHistFrame, this.mDensityFrame}, output);
        pushOutput(str, output);
        output.release();
    }

    private void createHistogramFrame(FilterContext context, int width, int height, int[] data) {
        int y;
        int i = width;
        int i2 = height;
        int[] histArray = new int[766];
        int y_border_thickness = (int) (((float) i2) * 1028443341);
        int x_border_thickness = (int) (((float) i) * 1028443341);
        int pixels = (i - (x_border_thickness * 2)) * (i2 - (y_border_thickness * 2));
        for (y = y_border_thickness; y < i2 - y_border_thickness; y++) {
            for (int x = x_border_thickness; x < i - x_border_thickness; x++) {
                int index = (y * i) + x;
                int energy = ((data[index] & 255) + ((data[index] >> 8) & 255)) + ((data[index] >> 16) & 255);
                histArray[energy] = histArray[energy] + 1;
            }
        }
        for (y = 1; y < 766; y++) {
            histArray[y] = histArray[y] + histArray[y - 1];
        }
        y = 0;
        while (y < 766) {
            histArray[y] = (int) ((((long) histArray[y]) * 65535) / ((long) pixels));
            y++;
            i = width;
            i2 = height;
        }
        FrameFormat shaderHistFormat = ImageFormat.create(766, 1, 3, 3);
        Frame frame = this.mHistFrame;
        if (frame != null) {
            frame.release();
        }
        this.mHistFrame = context.getFrameManager().newFrame(shaderHistFormat);
        this.mHistFrame.setInts(histArray);
    }
}
