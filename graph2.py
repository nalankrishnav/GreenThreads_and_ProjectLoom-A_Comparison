import numpy as np
import matplotlib.pyplot as plt


def plot_execution_times(execution_times):
    methods = list(execution_times.keys())
    categories = ["Matrix", "Merge", "Primes"]

    # Ensure each method has 3 values (Matrix, Merge, Primes)
    for method, times in execution_times.items():
        if len(times) != len(categories):
            print(
                f"Warning: {method} has {len(times)} values instead of {len(categories)}"
            )
            execution_times[method] = list(times) + [0] * (len(categories) - len(times))

    times_per_category = np.array(list(execution_times.values()))

    x = np.arange(len(categories))
    width = 0.2

    fig, ax = plt.subplots()

    for i, method in enumerate(methods):
        print(f"Method: {method}")
        print(f"x positions: {x} (len: {len(x)})")
        print(
            f"Execution times: {times_per_category[i]} (len: {len(times_per_category[i])})"
        )

        ax.bar(x + i * width, times_per_category[i], width=width, label=method)

    ax.set_xlabel("Computation Type")
    ax.set_ylabel("Execution Time (ms)")
    ax.set_title("Execution Time Comparison")
    ax.set_xticks(x + width)
    ax.set_xticklabels(categories)
    ax.legend()

    plt.show()
