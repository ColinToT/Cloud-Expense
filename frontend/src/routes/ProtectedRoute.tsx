import { Navigate, Outlet } from "react-router";
import { useAuth } from "@/auth/AuthContext";

const ProtectedRoute = () => {
  const { user, isInitializing } = useAuth();

  if (isInitializing) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
