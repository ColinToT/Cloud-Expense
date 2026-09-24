import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";
import { login } from "@/api/auth";
import { getCurrentUser } from "@/api/users";
import { clearAccessToken, getAccessToken, setAccessToken } from "@/auth/token";
import type { CurrentUser, LoginFormValues } from "@/types/auth";

interface AuthContextValue {
  user: CurrentUser | null;
  isInitializing: boolean;
  signIn: (values: LoginFormValues) => Promise<CurrentUser>;
  signOut: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [isInitializing, setIsInitializing] = useState(true);

  const signIn = useCallback(
    async (values: LoginFormValues): Promise<CurrentUser> => {
      const { accessToken } = await login(values);
      setAccessToken(accessToken);

      try {
        const currentUser = await getCurrentUser();
        setUser(currentUser);

        return currentUser;
      } catch (error) {
        clearAccessToken();
        setUser(null);

        throw error;
      }
    },
    [],
  );

  const signOut = useCallback(() => {
    clearAccessToken();
    setUser(null);
  }, []);

  useEffect(() => {
    async function restoreSession() {
      if (!getAccessToken()) {
        setIsInitializing(false);
        return;
      }

      try {
        const currentUser = await getCurrentUser();
        setUser(currentUser);
      } catch {
        clearAccessToken();
        setUser(null);
      } finally {
        setIsInitializing(false);
      }
    }

    void restoreSession();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        isInitializing,
        signIn,
        signOut,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}
