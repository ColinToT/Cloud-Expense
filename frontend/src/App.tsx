import { Navigate, Route, Routes } from "react-router";
import LoginPage from "@/pages/login/LoginPage";
import DashboardPage from "@/pages/dashboard/DashboardPage";
import ProtectedRoute from "@/routes/ProtectedRoute";
import MainLayout from "@/layouts/MainLayout";
import Expenses from "./pages/expenses/Expenses";
import ExpenseDetailPage from "./pages/expenses/ExpenseDetail";
import NewExpensePage from "./pages/expenses/NewExpense";
import EditExpensePage from "./pages/expenses/EditExpense";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/expenses" element={<Expenses />} />
          <Route path="/expenses/:id" element={<ExpenseDetailPage />} />
          <Route path="/expenses/new" element={<NewExpensePage />} />
          <Route path="/expenses/:id/edit" element={<EditExpensePage />} />
        </Route>
      </Route>

      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default App;
