import sys

# for example only
import time


class ProgressBar(object):
    # Creates a progress bar on the command line

    def __init__(self, total):
        self.total = total
        self.cur = 0
        self.bar_size = 40
        self.draw_bar()

    def update(self, amount=1):
        self.cur += amount
        self.draw_bar()
        if self.cur == self.total:
            print "\nDONE"

    def draw_bar(self):
        percent = float(self.cur) / self.total
        hashes = '#' * int(round(percent * self.bar_size))
        spaces = ' ' * (self.bar_size - len(hashes))
        sys.stdout.write("\rPercent: [{0}] {1}%".format(
            hashes + spaces, int(round(percent * 100))))
        sys.stdout.flush()

if __name__ == "__main__":
    b = ProgressBar(100)
    for x in range(100):
        b.update(1)
        time.sleep(.2)
