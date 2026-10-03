import { useEffect, useState } from "react";
import { Button, DatePicker, message, Select, Table } from "antd";
import type { ColumnsType } from "antd/es/table";
import type { Dayjs } from "dayjs";
import { DownloadOutlined } from "@ant-design/icons";

import { useAuth } from "@/auth/AuthContext";
import { exportExpenseReports, getExpenseReports } from "@/api/report";
import type { ExpenseReport, ExpenseReportQuery } from "@/types/report";
import type { ExpenseStatus } from "@/types/expense";
import ExpenseStatusTag from "@/components/ExpenseStatusTag/ExpenseStatusTag";

import "./Reports.css";

const { RangePicker } = DatePicker;

const Reports = () => {
  const { user } = useAuth();

  const [reports, setReports] = useState<ExpenseReport[]>([]);
  const [loading, setLoading] = useState(true);
  const [exporting, setExporting] = useState(false);

  const [dateRange, setDateRange] = useState<
    [Dayjs | null, Dayjs | null] | null
  >(null);

  const [status, setStatus] = useState<ExpenseStatus | undefined>();

  const buildQuery = (): ExpenseReportQuery => ({
    startDate: dateRange?.[0]?.format("YYYY-MM-DD"),
    endDate: dateRange?.[1]?.format("YYYY-MM-DD"),
    status,
  });

  const loadReports = async (query: ExpenseReportQuery = {}) => {
    try {
      setLoading(true);

      const data = await getExpenseReports(query);
      setReports(data);
    } catch {
      message.error("Failed to load expense reports.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadReports();
  }, []);

  const handleSearch = async () => {
    await loadReports(buildQuery());
  };

  const handleReset = async () => {
    setDateRange(null);
    setStatus(undefined);

    await loadReports();
  };

  const handleExport = async () => {
    try {
      setExporting(true);

      const blob = await exportExpenseReports(buildQuery());

      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");

      link.href = url;
      link.download = "expenses-report.xlsx";

      document.body.appendChild(link);
      link.click();
      link.remove();

      URL.revokeObjectURL(url);

      message.success("Report exported successfully.");
    } catch {
      message.error("Failed to export report.");
    } finally {
      setExporting(false);
    }
  };

  const columns: ColumnsType<ExpenseReport> = [
    {
      title: "Employee",
      dataIndex: "employeeName",
      key: "employeeName",
    },
    {
      title: "Expense",
      dataIndex: "title",
      key: "title",
    },
    {
      title: "Amount",
      key: "amount",
      render: (_, report) => `${report.currency} ${report.amount.toFixed(2)}`,
    },
    {
      title: "Expense date",
      dataIndex: "expenseDate",
      key: "expenseDate",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (value: ExpenseStatus) => <ExpenseStatusTag status={value} />,
    },
  ];

  const pageContent = {
    EMPLOYEE: {
      title: "My reports",
      description: "Review and export your expense records.",
    },
    MANAGER: {
      title: "Team reports",
      description: "Review and export your team's expense records.",
    },
    FINANCE: {
      title: "Expense reports",
      description: "Review and export organization expense records.",
    },
  } as const;

  const content =
    user?.role && user.role in pageContent
      ? pageContent[user.role as keyof typeof pageContent]
      : pageContent.EMPLOYEE;

  return (
    <div className="reports-page">
      <div className="reports-header">
        <div>
          <h1>{content.title}</h1>
          <p>{content.description}</p>
        </div>

        <Button
          type="primary"
          icon={<DownloadOutlined />}
          loading={exporting}
          onClick={handleExport}
        >
          Export Excel
        </Button>
      </div>

      <div className="reports-filter-card">
        <div className="reports-filters">
          <div className="reports-filter">
            <label>Date range</label>

            <RangePicker
              value={dateRange}
              onChange={(dates) => setDateRange(dates)}
            />
          </div>

          <div className="reports-filter">
            <label>Status</label>

            <Select
              allowClear
              placeholder="All statuses"
              value={status}
              onChange={(value: ExpenseStatus | undefined) => setStatus(value)}
              options={[
                { label: "Draft", value: "DRAFT" },
                { label: "Submitted", value: "SUBMITTED" },
                {
                  label: "Manager approved",
                  value: "MANAGER_APPROVED",
                },
                {
                  label: "Finance approved",
                  value: "FINANCE_APPROVED",
                },
                { label: "Rejected", value: "REJECTED" },
                { label: "Paid", value: "PAID" },
              ]}
            />
          </div>

          <div className="reports-filter-actions">
            <Button type="primary" onClick={handleSearch}>
              Apply filters
            </Button>

            <Button onClick={handleReset}>Reset</Button>
          </div>
        </div>
      </div>

      <div className="reports-table-card">
        <Table
          rowKey="expenseId"
          columns={columns}
          dataSource={reports}
          loading={loading}
          pagination={{
            pageSize: 10,
            showSizeChanger: false,
          }}
        />
      </div>
    </div>
  );
};

export default Reports;
