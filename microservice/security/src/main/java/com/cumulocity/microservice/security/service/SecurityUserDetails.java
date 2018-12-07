package com.cumulocity.microservice.security.service;

import com.cumulocity.common.auth.CumulocityCredentialsTransformer;
import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityCredentialsFactory;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

public class SecurityUserDetails implements UserDetails {
    @java.beans.ConstructorProperties({"authorities", "tenant", "password", "username", "oAuthAccessToken", "xsrfToken", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
    SecurityUserDetails(Collection<? extends GrantedAuthority> authorities, String tenant, String password, String username, String oAuthAccessToken, String xsrfToken, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.credentials = new CumulocityCredentialsFactory()
                .withOAuthAccessToken(oAuthAccessToken)
                .withXsrfToken(xsrfToken)
                .withTenant(tenant)
                .withUsername(username)
                .withPassword(password)
                .getCredentials();
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    private SecurityUserDetails(Collection<? extends GrantedAuthority> authorities, CumulocityCredentials credentials, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.credentials = credentials;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static SecurityUserDetails activeUser(String tenant, String username, String password, String... userRoles) {
        return activeUser(tenant, username, password, newArrayList(userRoles));
    }

    public static SecurityUserDetails activeUser(String tenant, String username, String password, Iterable<String> userRoles) {
        return activeUser(userRoles)
                .credentials(CumulocityBasicCredentials.builder()
                        .tenantId(tenant)
                        .username(username)
                        .password(password)
                        .build())
                .build();
    }

    public static SecurityUserDetails.SecurityUserDetailsBuilder activeUser(Iterable<String> userRoles) {
        final SecurityUserDetailsBuilder result = SecurityUserDetails.builder()
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true);
        for (final String userRole : userRoles) {
            result.authority(new SimpleGrantedAuthority(userRole));
        }
        return result;
    }

    private Collection<? extends GrantedAuthority> authorities;
    private CumulocityCredentials credentials;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public static SecurityUserDetailsBuilder builder() {
        return new SecurityUserDetailsBuilder();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getTenant() {
        return this.credentials.getTenantId();
    }

    public String getPassword() {
        if (credentials instanceof CumulocityBasicCredentials) {
            return ((CumulocityBasicCredentials) credentials).getPassword();
        }
        throw new IllegalStateException();
    }

    public String getUsername() {
        return this.credentials.getUsername();
    }

    public String getOAuthAccessToken() {
        if (credentials instanceof CumulocityOAuthCredentials) {
            return CumulocityCredentialsTransformer.toAuthorization(credentials);
        }
        throw new IllegalStateException();
    }

    public String getXsrfToken() {
        if (credentials instanceof CumulocityOAuthCredentials) {
            return ((CumulocityOAuthCredentials) credentials).getXsrfToken();
        }
        throw new IllegalStateException();
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SecurityUserDetails)) return false;
        final SecurityUserDetails other = (SecurityUserDetails) o;
        final Object this$authorities = this.getAuthorities();
        final Object other$authorities = other.getAuthorities();
        if (this$authorities == null ? other$authorities != null : !this$authorities.equals(other$authorities))
            return false;
        final Object this$tenant = this.getTenant();
        final Object other$tenant = other.getTenant();
        if (this$tenant == null ? other$tenant != null : !this$tenant.equals(other$tenant)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$oAuthAccessToken = this.getOAuthAccessToken();
        final Object other$oAuthAccessToken = other.getOAuthAccessToken();
        if (this$oAuthAccessToken == null ? other$oAuthAccessToken != null : !this$oAuthAccessToken.equals(other$oAuthAccessToken))
            return false;
        final Object this$xsrfToken = this.getXsrfToken();
        final Object other$xsrfToken = other.getXsrfToken();
        if (this$xsrfToken == null ? other$xsrfToken != null : !this$xsrfToken.equals(other$xsrfToken)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        if (this.isAccountNonExpired() != other.isAccountNonExpired()) return false;
        if (this.isAccountNonLocked() != other.isAccountNonLocked()) return false;
        if (this.isCredentialsNonExpired() != other.isCredentialsNonExpired()) return false;
        if (this.isEnabled() != other.isEnabled()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $authorities = this.getAuthorities();
        result = result * PRIME + ($authorities == null ? 43 : $authorities.hashCode());
        final Object $tenant = this.getTenant();
        result = result * PRIME + ($tenant == null ? 43 : $tenant.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $oAuthAccessToken = this.getOAuthAccessToken();
        result = result * PRIME + ($oAuthAccessToken == null ? 43 : $oAuthAccessToken.hashCode());
        final Object $xsrfToken = this.getXsrfToken();
        result = result * PRIME + ($xsrfToken == null ? 43 : $xsrfToken.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        result = result * PRIME + (this.isAccountNonExpired() ? 79 : 97);
        result = result * PRIME + (this.isAccountNonLocked() ? 79 : 97);
        result = result * PRIME + (this.isCredentialsNonExpired() ? 79 : 97);
        result = result * PRIME + (this.isEnabled() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "SecurityUserDetails(authorities=" + this.getAuthorities() + ", tenant=" + this.getTenant() + ", password=" + this.getPassword() + ", username=" + this.getUsername() + ", accountNonExpired=" + this.isAccountNonExpired() + ", accountNonLocked=" + this.isAccountNonLocked() + ", credentialsNonExpired=" + this.isCredentialsNonExpired() + ", enabled=" + this.isEnabled() + ")";
    }

    public static class SecurityUserDetailsBuilder {
        private ArrayList<GrantedAuthority> authorities;
        private CumulocityCredentials credentials;
        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private boolean enabled;

        SecurityUserDetailsBuilder() {
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder authority(GrantedAuthority authority) {
            if (this.authorities == null) this.authorities = new ArrayList<GrantedAuthority>();
            this.authorities.add(authority);
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            if (this.authorities == null) this.authorities = new ArrayList<GrantedAuthority>();
            this.authorities.addAll(authorities);
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder clearAuthorities() {
            if (this.authorities != null)
                this.authorities.clear();

            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder credentials(CumulocityCredentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public SecurityUserDetails.SecurityUserDetailsBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public SecurityUserDetails build() {
            Collection<GrantedAuthority> authorities;
            switch (this.authorities == null ? 0 : this.authorities.size()) {
                case 0:
                    authorities = java.util.Collections.emptyList();
                    break;
                case 1:
                    authorities = java.util.Collections.singletonList(this.authorities.get(0));
                    break;
                default:
                    authorities = java.util.Collections.unmodifiableList(new ArrayList<GrantedAuthority>(this.authorities));
            }

            return new SecurityUserDetails(authorities, credentials, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        }

        public String toString() {
            return "SecurityUserDetails.SecurityUserDetailsBuilder(authorities=" + this.authorities + ", tenant=" + this.credentials.getTenantId() + ", credentials=" + this.credentials + ", accountNonExpired=" + this.accountNonExpired + ", accountNonLocked=" + this.accountNonLocked + ", credentialsNonExpired=" + this.credentialsNonExpired + ", enabled=" + this.enabled + ")";
        }
    }
}
