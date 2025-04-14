import matplotlib.pyplot as plt
import re


def read_execution_times(filename):
    execution_times = {}

    with open(filename, "r") as file:
        for line in file:
            # Match each line with a format like: "Round-Robin - Matrix: 32 Merge: 123 Primes: 28"
            match = re.match(
                r"(\w+[\s\w]*) - Matrix: (\d+) Merge: (\d+) Primes: (\d+)", line
            )
            if match:
                method = match.group(1)
                matrix_time = int(match.group(2))
                merge_time = int(match.group(3))
                prime_time = int(match.group(4))
                execution_times[method] = [matrix_time, merge_time, prime_time]

    return execution_times


def plot_execution_times(execution_times):
    categories = ["Matrix Multiplication", "Merge Sort", "Prime Computation"]
    methods = list(execution_times.keys())

    # Extract execution times for each category
    times = [execution_times[method] for method in methods]

    # Transpose to get times per category
    times_per_category = list(zip(*times))

    x = range(len(categories))
    width = 0.2  # Width of bars

    plt.figure(figsize=(10, 6))

    # Plot each method with bars
    for i, method in enumerate(methods):
        plt.bar(
            [p + i * width for p in x], times_per_category[i], width=width, label=method
        )

    plt.xlabel("Computation Task")
    plt.ylabel("Execution Time (ms)")
    plt.title("Execution Time Comparison")
    plt.xticks([p + width for p in x], categories)
    plt.legend()
    plt.show()


# File containing execution times
filename = "execution_times.txt"

execution_times = read_execution_times(filename)
plot_execution_times(execution_times)
