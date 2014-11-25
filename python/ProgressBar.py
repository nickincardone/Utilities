import sys
import datetime
# for example only
import time


class ProgressBar(object):
    # Creates a progress bar on the command line

    def __init__(self, total):
        self.total = total
        self.cur = 0
        self.bar_size = 50
        #self.start_time = datetime.datetime.now()
        self.percent = 999
        self.draw_bar()

    def update(self, amount=1):
        self.cur += amount
        if self.cur > self.total:
            self.cur = self.total
        if self.cur < 0:
            self.cur = 0
        self.draw_bar()
        if self.cur == self.total:
            print "\nDONE"

    def draw_bar(self):
        percent = float(self.cur) / self.total
        hashes = '#' * int(round(percent * self.bar_size))
        spaces = ' ' * (self.bar_size - len(hashes))

        #time_diff = datetime.datetime.now() - self.start_time
        # if not percent == 0:
        #    est_time_remaining = time_diff.total_seconds() / percent - time_diff.total_seconds()
        # else:
        #    est_time_remaining = 60
        #mins_remaining = divmod(est_time_remaining, 60)[0]
        # sys.stdout.write("\rPercent: [{0}] {1}% {2} Minutes Left".format(
        #    hashes + spaces, int(round(percent * 100)), mins_remaining))

        if percent != self.percent:
            sys.stdout.write("\rPercent: [{0}] {1:0.1f}%".format(
                hashes + spaces, percent * 100))
            sys.stdout.flush()
        self.percent = percent

if __name__ == "__main__":
    bar = ProgressBar(78)
    for x in range(78):
        bar.update()
        time.sleep(.5)
