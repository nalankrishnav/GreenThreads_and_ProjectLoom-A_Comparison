import numpy as np
import matplotlib.pyplot as plt


# Function to read execution times from the file
def read_execution_times(filename):
    execution_times = {}

    with open(filename, "r") as file:
        lines = file.readlines()

        for line in lines[1:]:  # Skip the first line (header)
            parts = line.strip().split(" - ")
            if len(parts) < 2:
                continue  # Skip empty or malformed lines

            method = parts[0].strip()  # Extract the method name
            times = []

            # Extract numerical values
            segments = parts[1].split()
            for i in range(
                1, len(segments), 2
            ):  # Look at values after "Matrix:", "Merge:", "Primes:"
                try:
                    times.append(int(segments[i]))  # Convert to integer
                except ValueError:
                    times.append(0)  # Default to 0 if parsing fails

            # Ensure exactly 3 values are captured (Matrix, Merge, Primes)
            if len(times) == 3:
                execution_times[method] = times

    return execution_times


# Function to plot execution times
def plot_execution_times(execution_times):
    methods = list(
        execution_times.keys()
    )  # ["Round-Robin", "Threads", "Virtual Threads"]
    categories = ["Matrix", "Merge", "Primes"]

    if not execution_times:
        print("No valid execution times found. Check file format.")
        return

    times_per_category = np.array(
        list(execution_times.values())
    )  # Convert to NumPy array
    x = np.arange(len(categories))  # [0, 1, 2] for "Matrix", "Merge", "Primes"

    width = 0.2  # Width of each bar
    fig, ax = plt.subplots()

    # Plot each method at different positions
    for i, method in enumerate(methods):
        ax.bar(x + i * width, times_per_category[i], width=width, label=method)

    # Labeling and formatting
    ax.set_xlabel("Computation Type")
    ax.set_ylabel("Execution Time (ms)")
    ax.set_title("Execution Time Comparison")
    ax.set_xticks(x + width)  # Adjust x-ticks to align properly
    ax.set_xticklabels(categories)
    ax.legend()

    plt.show()


# Main execution
filename = "execution_times.txt"
execution_times = read_execution_times(filename)
plot_execution_times(execution_times)
