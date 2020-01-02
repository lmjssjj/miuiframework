package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.net.wifi.hotspot2.pps.Policy;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public final class PasspointConfiguration implements Parcelable {
    private static final int CERTIFICATE_SHA256_BYTES = 32;
    public static final Creator<PasspointConfiguration> CREATOR = new Creator<PasspointConfiguration>() {
        public PasspointConfiguration createFromParcel(Parcel in) {
            PasspointConfiguration config = new PasspointConfiguration();
            config.setHomeSp((HomeSp) in.readParcelable(null));
            config.setCredential((Credential) in.readParcelable(null));
            config.setPolicy((Policy) in.readParcelable(null));
            config.setSubscriptionUpdate((UpdateParameter) in.readParcelable(null));
            config.setTrustRootCertList(readTrustRootCerts(in));
            config.setUpdateIdentifier(in.readInt());
            config.setCredentialPriority(in.readInt());
            config.setSubscriptionCreationTimeInMillis(in.readLong());
            config.setSubscriptionExpirationTimeInMillis(in.readLong());
            config.setSubscriptionType(in.readString());
            config.setUsageLimitUsageTimePeriodInMinutes(in.readLong());
            config.setUsageLimitStartTimeInMillis(in.readLong());
            config.setUsageLimitDataLimit(in.readLong());
            config.setUsageLimitTimeLimitInMinutes(in.readLong());
            config.setServiceFriendlyNames((HashMap) in.readBundle().getSerializable("serviceFriendlyNames"));
            return config;
        }

        public PasspointConfiguration[] newArray(int size) {
            return new PasspointConfiguration[size];
        }

        private Map<String, byte[]> readTrustRootCerts(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<String, byte[]> trustRootCerts = new HashMap(size);
            for (int i = 0; i < size; i++) {
                trustRootCerts.put(in.readString(), in.createByteArray());
            }
            return trustRootCerts;
        }
    };
    private static final int MAX_URL_BYTES = 1023;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "PasspointConfiguration";
    private Credential mCredential = null;
    private int mCredentialPriority = Integer.MIN_VALUE;
    private HomeSp mHomeSp = null;
    private Policy mPolicy = null;
    private Map<String, String> mServiceFriendlyNames = null;
    private long mSubscriptionCreationTimeInMillis = Long.MIN_VALUE;
    private long mSubscriptionExpirationTimeInMillis = Long.MIN_VALUE;
    private String mSubscriptionType = null;
    private UpdateParameter mSubscriptionUpdate = null;
    private Map<String, byte[]> mTrustRootCertList = null;
    private int mUpdateIdentifier = Integer.MIN_VALUE;
    private long mUsageLimitDataLimit = Long.MIN_VALUE;
    private long mUsageLimitStartTimeInMillis = Long.MIN_VALUE;
    private long mUsageLimitTimeLimitInMinutes = Long.MIN_VALUE;
    private long mUsageLimitUsageTimePeriodInMinutes = Long.MIN_VALUE;

    public void setHomeSp(HomeSp homeSp) {
        this.mHomeSp = homeSp;
    }

    public HomeSp getHomeSp() {
        return this.mHomeSp;
    }

    public void setCredential(Credential credential) {
        this.mCredential = credential;
    }

    public Credential getCredential() {
        return this.mCredential;
    }

    public void setPolicy(Policy policy) {
        this.mPolicy = policy;
    }

    public Policy getPolicy() {
        return this.mPolicy;
    }

    public void setSubscriptionUpdate(UpdateParameter subscriptionUpdate) {
        this.mSubscriptionUpdate = subscriptionUpdate;
    }

    public UpdateParameter getSubscriptionUpdate() {
        return this.mSubscriptionUpdate;
    }

    public void setTrustRootCertList(Map<String, byte[]> trustRootCertList) {
        this.mTrustRootCertList = trustRootCertList;
    }

    public Map<String, byte[]> getTrustRootCertList() {
        return this.mTrustRootCertList;
    }

    public void setUpdateIdentifier(int updateIdentifier) {
        this.mUpdateIdentifier = updateIdentifier;
    }

    public int getUpdateIdentifier() {
        return this.mUpdateIdentifier;
    }

    public void setCredentialPriority(int credentialPriority) {
        this.mCredentialPriority = credentialPriority;
    }

    public int getCredentialPriority() {
        return this.mCredentialPriority;
    }

    public void setSubscriptionCreationTimeInMillis(long subscriptionCreationTimeInMillis) {
        this.mSubscriptionCreationTimeInMillis = subscriptionCreationTimeInMillis;
    }

    public long getSubscriptionCreationTimeInMillis() {
        return this.mSubscriptionCreationTimeInMillis;
    }

    public void setSubscriptionExpirationTimeInMillis(long subscriptionExpirationTimeInMillis) {
        this.mSubscriptionExpirationTimeInMillis = subscriptionExpirationTimeInMillis;
    }

    public long getSubscriptionExpirationTimeInMillis() {
        return this.mSubscriptionExpirationTimeInMillis;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.mSubscriptionType = subscriptionType;
    }

    public String getSubscriptionType() {
        return this.mSubscriptionType;
    }

    public void setUsageLimitUsageTimePeriodInMinutes(long usageLimitUsageTimePeriodInMinutes) {
        this.mUsageLimitUsageTimePeriodInMinutes = usageLimitUsageTimePeriodInMinutes;
    }

    public long getUsageLimitUsageTimePeriodInMinutes() {
        return this.mUsageLimitUsageTimePeriodInMinutes;
    }

    public void setUsageLimitStartTimeInMillis(long usageLimitStartTimeInMillis) {
        this.mUsageLimitStartTimeInMillis = usageLimitStartTimeInMillis;
    }

    public long getUsageLimitStartTimeInMillis() {
        return this.mUsageLimitStartTimeInMillis;
    }

    public void setUsageLimitDataLimit(long usageLimitDataLimit) {
        this.mUsageLimitDataLimit = usageLimitDataLimit;
    }

    public long getUsageLimitDataLimit() {
        return this.mUsageLimitDataLimit;
    }

    public void setUsageLimitTimeLimitInMinutes(long usageLimitTimeLimitInMinutes) {
        this.mUsageLimitTimeLimitInMinutes = usageLimitTimeLimitInMinutes;
    }

    public long getUsageLimitTimeLimitInMinutes() {
        return this.mUsageLimitTimeLimitInMinutes;
    }

    public void setServiceFriendlyNames(Map<String, String> serviceFriendlyNames) {
        this.mServiceFriendlyNames = serviceFriendlyNames;
    }

    public Map<String, String> getServiceFriendlyNames() {
        return this.mServiceFriendlyNames;
    }

    public String getServiceFriendlyName() {
        Map map = this.mServiceFriendlyNames;
        if (map == null || map.isEmpty()) {
            return null;
        }
        String friendlyName = (String) this.mServiceFriendlyNames.get(Locale.getDefault().getLanguage());
        if (friendlyName != null) {
            return friendlyName;
        }
        friendlyName = (String) this.mServiceFriendlyNames.get("en");
        if (friendlyName != null) {
            return friendlyName;
        }
        Map map2 = this.mServiceFriendlyNames;
        return (String) map2.get(map2.keySet().stream().findFirst().get());
    }

    public PasspointConfiguration(PasspointConfiguration source) {
        if (source != null) {
            HomeSp homeSp = source.mHomeSp;
            if (homeSp != null) {
                this.mHomeSp = new HomeSp(homeSp);
            }
            Credential credential = source.mCredential;
            if (credential != null) {
                this.mCredential = new Credential(credential);
            }
            Policy policy = source.mPolicy;
            if (policy != null) {
                this.mPolicy = new Policy(policy);
            }
            Map map = source.mTrustRootCertList;
            if (map != null) {
                this.mTrustRootCertList = Collections.unmodifiableMap(map);
            }
            UpdateParameter updateParameter = source.mSubscriptionUpdate;
            if (updateParameter != null) {
                this.mSubscriptionUpdate = new UpdateParameter(updateParameter);
            }
            this.mUpdateIdentifier = source.mUpdateIdentifier;
            this.mCredentialPriority = source.mCredentialPriority;
            this.mSubscriptionCreationTimeInMillis = source.mSubscriptionCreationTimeInMillis;
            this.mSubscriptionExpirationTimeInMillis = source.mSubscriptionExpirationTimeInMillis;
            this.mSubscriptionType = source.mSubscriptionType;
            this.mUsageLimitDataLimit = source.mUsageLimitDataLimit;
            this.mUsageLimitStartTimeInMillis = source.mUsageLimitStartTimeInMillis;
            this.mUsageLimitTimeLimitInMinutes = source.mUsageLimitTimeLimitInMinutes;
            this.mUsageLimitUsageTimePeriodInMinutes = source.mUsageLimitUsageTimePeriodInMinutes;
            this.mServiceFriendlyNames = source.mServiceFriendlyNames;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mHomeSp, flags);
        dest.writeParcelable(this.mCredential, flags);
        dest.writeParcelable(this.mPolicy, flags);
        dest.writeParcelable(this.mSubscriptionUpdate, flags);
        writeTrustRootCerts(dest, this.mTrustRootCertList);
        dest.writeInt(this.mUpdateIdentifier);
        dest.writeInt(this.mCredentialPriority);
        dest.writeLong(this.mSubscriptionCreationTimeInMillis);
        dest.writeLong(this.mSubscriptionExpirationTimeInMillis);
        dest.writeString(this.mSubscriptionType);
        dest.writeLong(this.mUsageLimitUsageTimePeriodInMinutes);
        dest.writeLong(this.mUsageLimitStartTimeInMillis);
        dest.writeLong(this.mUsageLimitDataLimit);
        dest.writeLong(this.mUsageLimitTimeLimitInMinutes);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serviceFriendlyNames", (HashMap) this.mServiceFriendlyNames);
        dest.writeBundle(bundle);
    }

    public boolean equals(Object thatObject) {
        boolean z = true;
        if (this == thatObject) {
            return true;
        }
        if (!(thatObject instanceof PasspointConfiguration)) {
            return false;
        }
        PasspointConfiguration that = (PasspointConfiguration) thatObject;
        HomeSp homeSp = this.mHomeSp;
        if (homeSp != null ? !homeSp.equals(that.mHomeSp) : that.mHomeSp != null) {
            Credential credential = this.mCredential;
            if (credential != null ? !credential.equals(that.mCredential) : that.mCredential != null) {
                Policy policy = this.mPolicy;
                if (policy != null ? !policy.equals(that.mPolicy) : that.mPolicy != null) {
                    UpdateParameter updateParameter = this.mSubscriptionUpdate;
                    if (updateParameter != null ? !updateParameter.equals(that.mSubscriptionUpdate) : that.mSubscriptionUpdate != null) {
                        if (isTrustRootCertListEquals(this.mTrustRootCertList, that.mTrustRootCertList) && this.mUpdateIdentifier == that.mUpdateIdentifier && this.mCredentialPriority == that.mCredentialPriority && this.mSubscriptionCreationTimeInMillis == that.mSubscriptionCreationTimeInMillis && this.mSubscriptionExpirationTimeInMillis == that.mSubscriptionExpirationTimeInMillis && TextUtils.equals(this.mSubscriptionType, that.mSubscriptionType) && this.mUsageLimitUsageTimePeriodInMinutes == that.mUsageLimitUsageTimePeriodInMinutes && this.mUsageLimitStartTimeInMillis == that.mUsageLimitStartTimeInMillis && this.mUsageLimitDataLimit == that.mUsageLimitDataLimit && this.mUsageLimitTimeLimitInMinutes == that.mUsageLimitTimeLimitInMinutes) {
                            Map map = this.mServiceFriendlyNames;
                            if (map != null) {
                            }
                        }
                    }
                }
            }
        }
        z = false;
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mHomeSp, this.mCredential, this.mPolicy, this.mSubscriptionUpdate, this.mTrustRootCertList, Integer.valueOf(this.mUpdateIdentifier), Integer.valueOf(this.mCredentialPriority), Long.valueOf(this.mSubscriptionCreationTimeInMillis), Long.valueOf(this.mSubscriptionExpirationTimeInMillis), Long.valueOf(this.mUsageLimitUsageTimePeriodInMinutes), Long.valueOf(this.mUsageLimitStartTimeInMillis), Long.valueOf(this.mUsageLimitDataLimit), Long.valueOf(this.mUsageLimitTimeLimitInMinutes), this.mServiceFriendlyNames});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateIdentifier: ");
        builder.append(this.mUpdateIdentifier);
        String str = "\n";
        builder.append(str);
        builder.append("CredentialPriority: ");
        builder.append(this.mCredentialPriority);
        builder.append(str);
        builder.append("SubscriptionCreationTime: ");
        long j = this.mSubscriptionCreationTimeInMillis;
        Object obj = "Not specified";
        builder.append(j != Long.MIN_VALUE ? new Date(j) : obj);
        builder.append(str);
        builder.append("SubscriptionExpirationTime: ");
        j = this.mSubscriptionExpirationTimeInMillis;
        builder.append(j != Long.MIN_VALUE ? new Date(j) : obj);
        builder.append(str);
        builder.append("UsageLimitStartTime: ");
        j = this.mUsageLimitStartTimeInMillis;
        if (j != Long.MIN_VALUE) {
            obj = new Date(j);
        }
        builder.append(obj);
        builder.append(str);
        builder.append("UsageTimePeriod: ");
        builder.append(this.mUsageLimitUsageTimePeriodInMinutes);
        builder.append(str);
        builder.append("UsageLimitDataLimit: ");
        builder.append(this.mUsageLimitDataLimit);
        builder.append(str);
        builder.append("UsageLimitTimeLimit: ");
        builder.append(this.mUsageLimitTimeLimitInMinutes);
        builder.append(str);
        if (this.mHomeSp != null) {
            builder.append("HomeSP Begin ---\n");
            builder.append(this.mHomeSp);
            builder.append("HomeSP End ---\n");
        }
        if (this.mCredential != null) {
            builder.append("Credential Begin ---\n");
            builder.append(this.mCredential);
            builder.append("Credential End ---\n");
        }
        if (this.mPolicy != null) {
            builder.append("Policy Begin ---\n");
            builder.append(this.mPolicy);
            builder.append("Policy End ---\n");
        }
        if (this.mSubscriptionUpdate != null) {
            builder.append("SubscriptionUpdate Begin ---\n");
            builder.append(this.mSubscriptionUpdate);
            builder.append("SubscriptionUpdate End ---\n");
        }
        if (this.mTrustRootCertList != null) {
            builder.append("TrustRootCertServers: ");
            builder.append(this.mTrustRootCertList.keySet());
            builder.append(str);
        }
        if (this.mServiceFriendlyNames != null) {
            builder.append("ServiceFriendlyNames: ");
            builder.append(this.mServiceFriendlyNames);
        }
        return builder.toString();
    }

    public boolean validate() {
        UpdateParameter updateParameter = this.mSubscriptionUpdate;
        if (updateParameter == null || updateParameter.validate()) {
            return validateForCommonR1andR2(true);
        }
        return false;
    }

    public boolean validateForR2() {
        if (this.mUpdateIdentifier == Integer.MIN_VALUE) {
            return false;
        }
        UpdateParameter updateParameter = this.mSubscriptionUpdate;
        if (updateParameter == null || !updateParameter.validate()) {
            return false;
        }
        return validateForCommonR1andR2(false);
    }

    private boolean validateForCommonR1andR2(boolean isR1) {
        HomeSp homeSp = this.mHomeSp;
        if (homeSp == null || !homeSp.validate()) {
            return false;
        }
        Credential credential = this.mCredential;
        if (credential == null || !credential.validate(isR1)) {
            return false;
        }
        Policy policy = this.mPolicy;
        if (policy != null && !policy.validate()) {
            return false;
        }
        Map map = this.mTrustRootCertList;
        if (map != null) {
            for (Entry<String, byte[]> entry : map.entrySet()) {
                String url = (String) entry.getKey();
                byte[] certFingerprint = (byte[]) entry.getValue();
                boolean isEmpty = TextUtils.isEmpty(url);
                String str = TAG;
                StringBuilder stringBuilder;
                if (isEmpty) {
                    Log.d(str, "Empty URL");
                    return false;
                } else if (url.getBytes(StandardCharsets.UTF_8).length > 1023) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("URL bytes exceeded the max: ");
                    stringBuilder.append(url.getBytes(StandardCharsets.UTF_8).length);
                    Log.d(str, stringBuilder.toString());
                    return false;
                } else if (certFingerprint == null) {
                    Log.d(str, "Fingerprint not specified");
                    return false;
                } else if (certFingerprint.length != 32) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Incorrect size of trust root certificate SHA-256 fingerprint: ");
                    stringBuilder.append(certFingerprint.length);
                    Log.d(str, stringBuilder.toString());
                    return false;
                }
            }
        }
        return true;
    }

    private static void writeTrustRootCerts(Parcel dest, Map<String, byte[]> trustRootCerts) {
        if (trustRootCerts == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(trustRootCerts.size());
        for (Entry<String, byte[]> entry : trustRootCerts.entrySet()) {
            dest.writeString((String) entry.getKey());
            dest.writeByteArray((byte[]) entry.getValue());
        }
    }

    private static boolean isTrustRootCertListEquals(Map<String, byte[]> list1, Map<String, byte[]> list2) {
        boolean z = true;
        if (list1 == null || list2 == null) {
            if (list1 != list2) {
                z = false;
            }
            return z;
        } else if (list1.size() != list2.size()) {
            return false;
        } else {
            for (Entry<String, byte[]> entry : list1.entrySet()) {
                if (!Arrays.equals((byte[]) entry.getValue(), (byte[]) list2.get(entry.getKey()))) {
                    return false;
                }
            }
            return true;
        }
    }
}
