// Maps to the Java DTO representing individual category slices
export interface CategoryExpense {
  category: string;
  amount: number;
}

// Maps to the Java DTO representing the entire dashboard payload
export interface DashboardSummary {
  totalBalance: number;
  monthlyExpenses: number;
  expenseBreakdown: CategoryExpense[];
}