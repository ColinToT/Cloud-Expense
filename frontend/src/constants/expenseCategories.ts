export const EXPENSE_CATEGORIES = [
  { value: 1, label: "Meals" },
  { value: 2, label: "Travel" },
  { value: 3, label: "Hotel" },
  { value: 4, label: "Other" },
];

export const getExpenseCategoryLabel = (categoryId: number) => {
  return (
    EXPENSE_CATEGORIES.find((category) => category.value === categoryId)
      ?.label ?? "Unknown"
  );
};
