package android.security.keystore.recovery;

import android.util.ArrayMap;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public final class TrustedRootCertificates {
    private static final ArrayMap<String, X509Certificate> ALL_ROOT_CERTIFICATES = constructRootCertificateMap();
    public static final String GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_ALIAS = "GoogleCloudKeyVaultServiceV1";
    private static final String GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_BASE64 = "MIIFDzCCAvegAwIBAgIQbNdueU2o0vM9gGq4N6bhjzANBgkqhkiG9w0BAQsFADAxMS8wLQYDVQQDEyZHb29nbGUgQ2xvdWQgS2V5IFZhdWx0IFNlcnZpY2UgUm9vdCBDQTAeFw0xODA1MDcxODI0MDJaFw0zODA1MDgxOTI0MDJaMDExLzAtBgNVBAMTJkdvb2dsZSBDbG91ZCBLZXkgVmF1bHQgU2VydmljZSBSb290IENBMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEArUgzu+4o9yl22eql1BiGBq3gWXooh2ql3J+vVuzf/ThjzdIg0xkkkw/NAFxYFi49Eo1fa/hf8wCIoAqCEs1lD6tE3cCD3T3+EQPquh6CB2KmZDJ6mPnXvVUlUuFr0O2MwZkwylqBETzK0x5NCHgL/p47vkjhHx6LqVaobigKlHxszvVi4fkt/qq7KW3YTVxhwdLGEab+OqSfwMxdBLhMfE0K0dvFt8bs8yJAF04DJsMbRChFFBpT17Z0u53iIAAu5qVQhKrQXiIAwgboZqd+JkHLXU1fJeVT5WJOJgoJFWHkdWkHta4mSYlS72J1Q927JD1JdET1kFtH+EDtYAtx7x7F9xAAbb2tMITws/wwd2rAzZTX/kxRbDlXVLToU05LFYPr+dFV1wvXmi0jlkIxnhdaVBqWC93p528UiUcLpib+HVzMWGdYI3G1NOa/lTp0c8LcbJjapiiVneRQJ3cIqDPOSEnEq40hyZd1jx3JnOxJMwHs8v4s9GIlb3BcOmDvA/Mu09xEMKwpHBm4TFDKXeGHOWha7ccWEECbyO5ncu6XuN2iyz9S+TuMyjZBE552p6Pu5gEC2xk+qab0NGDTHdLKLbyWn3IxdmBHyTr7iPCqmpyHngkC/pbGfvGusc5BpBugsBtlz67m4RWLJ72yAeVPO/ly/8w4orNsGWjn3s0CAwEAAaMjMCEwDgYDVR0PAQH/BAQDAgGGMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggIBAGiWlu+4qyxgPb6RsA0mwR7V21UJ9rEpYhSN+ARpTWGiI22RCJSGK0ZrPGeFQzE2BpnVRdmLTV5jf9JUStjHoPvNYFnwLTJ0E2e9Olj8MrHrAucAUFLhl4woWz0kU/X0EB1j6Y2SXrAaZPiMMpq8BKj3mH1MbV4stZ0kiHUpZu6PEmrojYG7FKKN30na2xXfiOfl2JusVsyHDqmUn/HjTh6zASKqE6hxE+FJRl2VQ4dcr4SviHtdbimMy2LghLnZ4FE4XhJgRnw9TeRV5C9Sn7pmnAA5X0C8ZXhXvfvrdx4fL3UKlk1Lqlb5skxoK1R9wwr+aNIO+cuR8JA5DmEDWFw5Budh/uWWZlBTyVW2ybbTB6tkmOc8c08XOgxBaKrsXALmJcluabjmN1jp81ae1epeN31jJ4N5IE5aq7XbTFmKkwpgTTvJmqCR2XzWujlvdbdjfiABliWsnLzLQCP8eZwcM4LA5UK3f1ktHolr1OI9etSOkebE2py8LPYBJWlX36tRAagZhU/NoyOtvhRzq9rb3rbf96APEHKUFsXG9nBEd2BUKZghLKPf+JNCU/2pOGx0jdMcf+K+a1DeG0YzGYMRkFvpN3hvHYrJdByL3kSP3UtD0H2g8Ps7gRLELG2HODxbSn8PV3XtuSvxVanA6uyaaS3AZ6SxeVLvmw507aYI";
    private static final X509Certificate GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_CERTIFICATE = parseBase64Certificate(GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_BASE64);
    public static final String INSECURE_KEY_ALIAS_PREFIX = "INSECURE_KEY_ALIAS_KEY_MATERIAL_IS_NOT_PROTECTED_";
    public static final String INSECURE_PASSWORD_PREFIX = "INSECURE_PSWD_";
    private static final int NUMBER_OF_ROOT_CERTIFICATES = 1;
    public static final String TEST_ONLY_INSECURE_CERTIFICATE_ALIAS = "TEST_ONLY_INSECURE_CERTIFICATE_ALIAS";
    private static final String TEST_ONLY_INSECURE_CERTIFICATE_BASE64 = "MIIFMDCCAxigAwIBAgIJAIZ9/G8KQie9MA0GCSqGSIb3DQEBDQUAMCUxIzAhBgNVBAMMGlRlc3QgT25seSBVbnNlY3VyZSBSb290IENBMB4XDTE4MDMyODAwMzIyM1oXDTM4MDMyMzAwMzIyM1owJTEjMCEGA1UEAwwaVGVzdCBPbmx5IFVuc2VjdXJlIFJvb3QgQ0EwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQDGxFNzAEyzSPmwE5gfuBXdXq++bl9Ep62V7Xn1UiejvmS+pRHT39pf/M7sl4Zr9ezanJTrFvf9+B85VGehdsD32TgfEjThcqaoQCI6pKkHYsUo7FZ5n+G3eE8oabWRZJMVo3QDjnnFYp7z20vnpjDofI2oQyxHcb/1yep+ca1+4lIvbUp/ybhNFqhRXAMcDXo7pyH38eUQ1JdKQ/QlBbShpFEqx1Y6KilKfTDf7Wenqr67LkaEim//yLZjlHzn/BpuRTrpo+XmJZx1P9CX9LGOXTtmsaCcYgD4yijOvV8aEsIJaf1kCIO558oH0oQc+0JG5aXeLN7BDlyZvH0RdSx5nQLS9kj2I6nthOw/q00/L+S6A0m5jyNZOAl1SY78p+wO0d9eHbqQzJwfEsSq3qGAqlgQyyjp6oxHBqT9hZtN4rxw+iq0K1S4kmTLNF1FvmIB1BE+lNvvoGdY5G0b6Pe4R5JFn9LV3C3PEmSYnae7iG0IQlKmRADIuvfJ7apWAVanJPJAAWh2Akfp8Uxr02cHoY6o7vsEhJJOeMkipaBHThESm/XeFVubQzNfZ9gjQnB9ZX2v+lyj+WYZSAz3RuXx6TlLrmWccMpQDR1ibcgyyjLUtX3kwZl2OxmJXitjuD7xlxvAXYob15N+K4xKHgxUDrbt2zU/tY0vgepAUg/xbwIDAQABo2MwYTAdBgNVHQ4EFgQUwyeNpYgsXXYvh9z0/lFrja7sV+swHwYDVR0jBBgwFoAUwyeNpYgsXXYvh9z0/lFrja7sV+swDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMCAYYwDQYJKoZIhvcNAQENBQADggIBAGuOsvMN5SD3RIQnMJtBpcHNrxun+QFjPZFlYCLfIPrUkHpn5O1iIIq8tVLd2V+12VKnToUEANsYBD3MP8XjP+6GZ7ZQ2rwLGvUABKSX4YXvmjEEXZUZp0y3tIV4kUDlbACzguPneZDp5Qo7YWH4orgqzHkn0sD/ikO5XrAqmzc245ewJlrf+V11mjcuELfDrEejpPhi7Hk/ZNR0ftP737Hs/dNoCLCIaVNgYzBZhgo4kd220TeJu2ttW0XZldyShtpcOmyWKBgVseixR6L/3sspPHyAPXkSuRo0Eh1xvzDKCg9ttb0qoacTlXMFGkBpNzmVq67NWFGGa9UElift1mv6RfktPCAGZ+Ai8xUiKAUB0Eookpt/8gX9SenqyP/jMxkxXmHWxUu8+KnLvj6WLrfftuuD7u3cfc7j5kkrheDz3O4h4477GnqL5wdo9DuEsNc4FxJVz8Iy8RS6cJuW4pihYpM1Tyn7uopLnImpYzEY+R5aQqqr+q/A1diqogbEKPH6oUiqJUwq3nD70gPBUKJmIzS4vLwLouqUHEm1k/MgHV/BkEU0uVHszPFaXUMMCHb0iT9P8LuZ7Ajer3SR/0TRVApCrk/6OV68e+6k/OFpM5kcZnNMD5ANyBriTsz3NrDwSw4i4+Dsfh6A9dB/cEghw4skLaBxnQLQIgVeqCzK";

    public static X509Certificate getTestOnlyInsecureCertificate() {
        return parseBase64Certificate(TEST_ONLY_INSECURE_CERTIFICATE_BASE64);
    }

    public static Map<String, X509Certificate> getRootCertificates() {
        return new ArrayMap(ALL_ROOT_CERTIFICATES);
    }

    public static X509Certificate getRootCertificate(String alias) {
        return (X509Certificate) ALL_ROOT_CERTIFICATES.get(alias);
    }

    private static ArrayMap<String, X509Certificate> constructRootCertificateMap() {
        ArrayMap<String, X509Certificate> certificates = new ArrayMap(1);
        certificates.put(GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_ALIAS, GOOGLE_CLOUD_KEY_VAULT_SERVICE_V1_CERTIFICATE);
        return certificates;
    }

    private static X509Certificate parseBase64Certificate(String base64Certificate) {
        try {
            return X509CertificateParsingUtils.decodeBase64Cert(base64Certificate);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    private TrustedRootCertificates() {
    }
}
